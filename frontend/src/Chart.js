import React, { Component } from 'react';
import { Button, Col, Container, Row, Form, FormGroup, Label } from 'reactstrap';
import Chart from 'chart.js/auto';
import AppNavbar from "./AppNavbar";
import 'chartjs-adapter-luxon';
import DatePicker from "react-datepicker";
import 'react-datepicker/dist/react-datepicker.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Select, { components } from 'react-select';
import zoomPlugin from "chartjs-plugin-zoom";
import { SortableContainer, SortableElement, SortableHandle } from 'react-sortable-hoc';

Chart.register(zoomPlugin);

// Utility function to move array elements
function arrayMove(array, from, to) {
    const newArray = array.slice();
    newArray.splice(to < 0 ? newArray.length + to : to, 0, newArray.splice(from, 1)[0]);
    return newArray;
}

// Sortable MultiValue Component
const SortableMultiValue = SortableElement(props => {
    const onMouseDown = e => {
        e.preventDefault();
        e.stopPropagation();
    };
    const innerProps = { ...props.innerProps, onMouseDown };
    return <components.MultiValue {...props} innerProps={innerProps} />;
});

const SortableMultiValueLabel = SortableHandle(props => (
    <components.MultiValueLabel {...props} />
));

const SortableMultiSelect = SortableContainer(Select);

class ChartComponent extends Component {
    constructor(props) {
        super(props);

        this.state = {
            patients: [],
            fields: [],
            selectedPatient: null,
            startDate: null,
            endDate: null,
            selectedFields: [],
            chartData: { labels: [], datasets: [] },
            multiAxis: true, // Default to multi-axis line chart
        };

        this.chartRef = React.createRef();
        this.chartInstances = [];
        this.standardColors = ['#000000', '#FF0000', '#0000FF', '#00FF00', '#FFA500', '#800080', '#FFFF00', '#00FFFF', '#FFC0CB', '#A52A2A'];
    }

    componentDidMount() {
        fetch('/patient/get-all')
            .then(response => response.json())
            .then(data => {
                this.setState({ patients: data });
            });

        fetch('/fields/get-all')
            .then(response => response.json())
            .then(data => {
                this.setState({ fields: data });
            });
    }

    fetchChartData = () => {
        const { selectedPatient, startDate, endDate, selectedFields } = this.state;

        if (!selectedPatient || selectedFields.length === 0 || !startDate || !endDate || startDate > endDate) {
            return;
        }

        const correctedStartDate = this.formatDate(startDate);
        const correctedEndDate = this.formatDate(endDate);

        const url = `/eav/get-specific-exams?patient_id=${selectedPatient}&field_ids=${selectedFields.join(',')}&start_date=${correctedStartDate}&end_date=${correctedEndDate}`;

        fetch(url)
            .then(response => response.json())
            .then(data => {
                const formattedChartData = this.processChartData(data);
                this.setState({ chartData: formattedChartData }, () => this.renderChart());
            });
    };

    formatDate = (date) => {
        const year = date.getFullYear();
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const day = date.getDate().toString().padStart(2, '0');
        return `${year}-${month}-${day}`;
    };

    processChartData = (data) => {
        if (!Array.isArray(data)) {
            return { labels: [], datasets: [] };
        }

        const { fields, selectedFields } = this.state;

        const labels = data.reduce((result, entry) => {
            result[new Date(entry.id.exam_date).toISOString()] = null;
            return result;
        }, {});

        const datasets = selectedFields.map((selectedField, index) => {
            const field = fields.find(f => f.field_id === selectedField);
            const color = this.getStandardColor(index);

            const datasetData = data
                .filter(entry => entry.field.field_id === selectedField)
                .reduce((result, entry) => {
                    const exam_date = new Date(entry.id.exam_date).toISOString();
                    result[exam_date] = { val: entry.val, exam_date: exam_date };
                    return result;
                }, {});

            return {
                label: field.description_gr, // No numbering, just field description
                measurement_unit: field.measurement_unit,
                data: Object.values(datasetData).map(entry => entry.val),
                examDates: Object.values(datasetData).map(entry => entry.exam_date),
                borderColor: color,
                yAxisID: this.state.multiAxis ? `y-axis-${index}` : null,
            };
        });

        return {
            labels: Object.keys(labels),
            datasets,
        };
    };

    getStandardColor = (index) => {
        return this.standardColors[index % this.standardColors.length];
    };

    renderChart = () => {
        const { chartData, multiAxis } = this.state;

        // Clear existing charts
        this.chartInstances.forEach(chart => chart.destroy());
        this.chartInstances = [];

        // Get the parent container for the charts
        const chartContainer = this.chartRef.current;

        // Remove any existing child elements (previous charts)
        while (chartContainer.firstChild) {
            chartContainer.removeChild(chartContainer.firstChild);
        }

        // Set height based on chart type
        const chartHeight = multiAxis ? 600 : 400;

        if (multiAxis) {
            // Render Multi-Axis Chart
            const canvas = document.createElement('canvas');
            canvas.style.width = '90vw';
            canvas.height = chartHeight;  // Set height for multi-axis chart
            chartContainer.appendChild(canvas);

            const chartInstance = new Chart(canvas.getContext('2d'), {
                type: 'line',
                data: {
                    datasets: chartData.datasets.map(dataset => ({
                        label: dataset.label,
                        data: dataset.data.map((value, index) => ({ x: dataset.examDates[index], y: value })),
                        borderColor: dataset.borderColor,
                        fill: false,
                        tension: 0.1,
                        yAxisID: dataset.yAxisID,
                    })),
                },
                options: {
                    responsive: true,
                    interaction: {
                        mode: 'x',
                        intersect: false,
                    },
                    scales: {
                        x: {
                            type: 'time',
                            time: {
                                unit: 'day',
                                tooltipFormat: 'dd-MM-yyyy',
                            },
                            position: 'bottom',
                        },
                        ...chartData.datasets.reduce((yAxesConfig, dataset, index) => {
                            if (dataset.yAxisID) {
                                const color = dataset.borderColor;
                                yAxesConfig[`y-axis-${index}`] = {
                                    type: 'linear',
                                    position: index % 2 === 0 ? 'left' : 'right',
                                    ticks: {
                                        callback: (val) => val.toFixed(1) + dataset.measurement_unit,
                                        color: color,
                                    },
                                };
                            }
                            return yAxesConfig;
                        }, {}),
                    },
                    plugins: {
                        zoom: {
                            pan: {
                                enabled: true,
                                mode: 'xy',
                            },
                            zoom: {
                                wheel: {
                                    enabled: true,
                                },
                                pinch: {
                                    enabled: true,
                                },
                                mode: 'xy',
                            },
                        },
                    },
                    grid: {
                        drawOnChartArea: false,
                    },
                },
            });

            this.chartInstances.push(chartInstance);
        } else {
            // Render Separate Charts
            chartData.datasets.forEach((dataset) => {
                const canvas = document.createElement('canvas');
                canvas.style.width = '90vw';
                canvas.height = chartHeight;  // Set height for separate charts
                chartContainer.appendChild(canvas);

                const chartInstance = new Chart(canvas.getContext('2d'), {
                    type: 'line',
                    data: {
                        datasets: [{
                            label: dataset.label,
                            data: dataset.data.map((value, index) => ({ x: dataset.examDates[index], y: value })),
                            borderColor: dataset.borderColor,
                            fill: false,
                            tension: 0.1,
                        }],
                    },
                    options: {
                        responsive: true,
                        interaction: {
                            mode: 'x',
                            intersect: false,
                        },
                        scales: {
                            x: {
                                type: 'time',
                                time: {
                                    unit: 'day',
                                    tooltipFormat: 'dd-MM-yyyy',
                                },
                                position: 'bottom',
                            },
                            y: {
                                type: 'linear',
                                position: 'left',
                                ticks: {
                                    callback: (val) => val.toFixed(1) + dataset.measurement_unit,
                                    color: dataset.borderColor,
                                },
                            },
                        },
                        plugins: {
                            zoom: {
                                pan: {
                                    enabled: true,
                                    mode: 'xy',
                                },
                                zoom: {
                                    wheel: {
                                        enabled: true,
                                    },
                                    pinch: {
                                        enabled: true,
                                    },
                                    mode: 'xy',
                                },
                            },
                        },
                        grid: {
                            drawOnChartArea: false,
                        },
                    },
                });
                this.chartInstances.push(chartInstance);
            });
        }
    };

    toggleChartType = () => {
        this.setState(prevState => ({ multiAxis: !prevState.multiAxis }), () => {
            this.fetchChartData(); // Re-fetch chart data to update the chart
        });
    };

    handleFieldSortEnd = ({ oldIndex, newIndex }) => {
        // Reorder fields and selectedFields
        const reorderedFields = arrayMove(this.state.fields, oldIndex, newIndex);
        const reorderedSelectedFields = arrayMove(this.state.selectedFields, oldIndex, newIndex);

        // Update state
        this.setState({
            fields: reorderedFields,
            selectedFields: reorderedSelectedFields
        }, () => {
            console.log('Updated Fields:', this.state.fields);
            console.log('Updated Selected Fields:', this.state.selectedFields);
            this.fetchChartData(); // Re-fetch chart data to update the chart
        });
    };

    render() {
        const { patients, startDate, endDate, selectedPatient, fields, selectedFields, multiAxis } = this.state;

        // Generate field options
        const fieldOptions = fields.map(field => ({
            value: field.field_id,
            label: field.description_gr
        }));

        // Generate selected field options based on the current selectedFields
        const selectedFieldOptions = fieldOptions.filter(option => selectedFields.includes(option.value));

        return (
            <div>
                <AppNavbar />
                <Container fluid>
                    <Row>
                        <Col md={4}>
                            <Form>
                                <FormGroup>
                                    <Label for="patientSelect">Patient</Label>
                                    <Select
                                        id="patientSelect"
                                        options={patients.map(patient => ({
                                            value: patient.patient_id,
                                            label: patient.patient_id
                                        }))}
                                        onChange={option => this.setState({ selectedPatient: option.value })}
                                        value={patients.find(patient => patient.patient_id === selectedPatient) ?
                                            { value: selectedPatient, label: selectedPatient } : null}
                                    />
                                </FormGroup>
                                <FormGroup>
                                    <Label for="startDate">Start Date</Label>
                                    <DatePicker
                                        id="startDate"
                                        selected={startDate}
                                        onChange={date => this.setState({ startDate: date })}
                                        dateFormat="yyyy-MM-dd"
                                    />
                                </FormGroup>
                                <FormGroup>
                                    <Label for="endDate">End Date</Label>
                                    <DatePicker
                                        id="endDate"
                                        selected={endDate}
                                        onChange={date => this.setState({ endDate: date })}
                                        dateFormat="yyyy-MM-dd"
                                    />
                                </FormGroup>
                                <FormGroup>
                                    <Label for="fieldSelect">Fields</Label>
                                    <SortableMultiSelect
                                        id="fieldSelect"
                                        options={fieldOptions}
                                        isMulti
                                        onChange={options => this.setState({ selectedFields: options.map(option => option.value) })}
                                        value={selectedFieldOptions}
                                        onSortEnd={this.handleFieldSortEnd}
                                        useDragHandle
                                        axis="xy"
                                        distance={4}
                                        getHelperDimensions={({ node }) => node.getBoundingClientRect()}
                                        components={{
                                            MultiValue: SortableMultiValue,
                                            MultiValueLabel: SortableMultiValueLabel
                                        }}
                                        closeMenuOnSelect={false}
                                    />
                                </FormGroup>
                                <Button color="primary" onClick={this.fetchChartData}>Fetch Data</Button>
                                <Button color="secondary" onClick={this.toggleChartType} style={{ marginLeft: '10px' }}>
                                    {multiAxis ? 'Show Separate Charts' : 'Show Multi-Axis Chart'}
                                </Button>
                            </Form>
                        </Col>
                        <Col md={8}>
                            <div ref={this.chartRef} style={{ width: '100%', height: '400px' }}></div>
                        </Col>
                    </Row>
                </Container>
            </div>
        );
    }
}

export default ChartComponent;

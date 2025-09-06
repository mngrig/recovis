import React, { Component } from 'react';
import { Button, Label, Input, FormGroup, Form, Col, Row, Container } from 'reactstrap';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import AppNavbar from './AppNavbar';
import { TableContainer, Table, TableHead, TableBody, TableRow, TableCell, Paper, TextField, Select, MenuItem, Pagination } from '@mui/material';
import './PatientExams.css';

class PatientExams extends Component {
    state = {
        startDate: null, // State for start date of the exam range
        endDate: null, // State for end date of the exam range
        patientId: '', // State for patient ID
        days: 7, // Default value for the 'days' input
        exams: [], // State to hold fetched exams data
        showTable: false, // State to control table visibility
        rowsPerPage: 20, // Default rows per page for the table
        tableWidth: 1500, // Default table width
        currentPage: 0, // Current page number for pagination
        isDaysValid: true, // Validation state for the 'days' input
        fieldOrder: [], // Array to hold the order of fields to display in the table
        noExamsMessage: false, // State to track if there are no exams
    };

    handleChange = (field, value) => {
        this.setState({ [field]: value }, () => {
            if (field === 'days') {
                const isDaysValid = Number.isInteger(Number(value)) && Number(value) > 0;
                this.setState({ isDaysValid });
            }
        });
    };

    handleSubmit = async (summarized = false) => {
        const { startDate, endDate, patientId, days, isDaysValid } = this.state;

        if (!isDaysValid && summarized) {
            alert('Please enter a valid number of days.');
            return;
        }

        this.setState({ exams: [], showTable: false, currentPage: 0, noExamsMessage: false });

        try {
            const correctedStartDate = this.formatDate(startDate);
            const correctedEndDate = this.formatDate(endDate);

            let url;
            if (summarized) {
                url = `/eav/get-summarized-exams?patient_id=${patientId}&start_date=${correctedStartDate}&end_date=${correctedEndDate}&days=${days}`;
            } else {
                url = `/eav/get-all-exams?patient_id=${patientId}&start_date=${correctedStartDate}&end_date=${correctedEndDate}`;
            }

            const response = await fetch(url);
            const data = await response.json();

            const groupedExams = {};
            const fieldOrder = [];

            data.forEach(({ id, val, field }) => {
                const examDate = id.exam_date;
                if (!groupedExams[examDate]) {
                    groupedExams[examDate] = [];
                }
                groupedExams[examDate].push({
                    val,
                    description_gr: field.description_gr,
                });
                if (!fieldOrder.includes(field.description_gr)) {
                    fieldOrder.push(field.description_gr);
                }
            });

            // Check if there are no exams and set the noExamsMessage flag
            if (Object.keys(groupedExams).length === 0) {
                this.setState({ noExamsMessage: true });
            } else {
                this.setState({ exams: groupedExams, showTable: true, fieldOrder });
            }
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };

    formatDate = (date) => {
        const year = date.getFullYear();
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const day = date.getDate().toString().padStart(2, '0');
        return `${year}-${month}-${day}`;
    };

    prepareData = () => {
        const { exams, rowsPerPage, currentPage, fieldOrder } = this.state;

        const allData = Object.entries(exams).map(([examDate, examsGroup]) => {
            const row = { examDate };
            examsGroup.forEach(subexam => {
                row[subexam.description_gr] = subexam.val;
            });
            return row;
        });

        const startIndex = currentPage * rowsPerPage;
        const endIndex = startIndex + rowsPerPage;

        return allData.slice(startIndex, endIndex);
    };

    prepareColumns = () => {
        const { fieldOrder } = this.state;

        const columns = [
            {
                Header: 'Exam Date',
                accessor: 'examDate',
                fixed: 'left', // Fix the first column
            },
        ];

        fieldOrder.forEach(description_gr => {
            columns.push({
                Header: description_gr,
                accessor: description_gr,
            });
        });

        return columns;
    };

    handlePageChange = (event, newPage) => {
        this.setState({ currentPage: newPage - 1 });
    };

    render() {
        const { startDate, endDate, patientId, days, showTable, rowsPerPage, tableWidth, currentPage, exams, isDaysValid, noExamsMessage } = this.state;
        const data = this.prepareData();
        const columns = this.prepareColumns();
        const totalPages = Math.ceil(Object.keys(exams).length / rowsPerPage);

        return (
            <div>
                <AppNavbar />
                <Container>
                    <h3 className="mb-4">Exam Page</h3>
                    <Form>
                        <Row form className='align-items-center'>
                            <Col md={2} className='mx-1'>
                                <FormGroup>
                                    <Label for="startDate">Start Date:</Label>
                                    <DatePicker
                                        utcOffset={0}
                                        id="startDate"
                                        selected={startDate}
                                        onChange={(date) => this.handleChange('startDate', date)}
                                        dateFormat="yyyy-MM-dd"
                                        className="form-control"
                                    />
                                </FormGroup>
                            </Col>
                            <Col md={2} className='mx-1'>
                                <FormGroup>
                                    <Label for="endDate">End Date:</Label>
                                    <DatePicker
                                        utcOffset={0}
                                        id="endDate"
                                        selected={endDate}
                                        onChange={(date) => this.handleChange('endDate', date)}
                                        dateFormat="yyyy-MM-dd"
                                        className="form-control"
                                    />
                                </FormGroup>
                            </Col>
                            <Col md={2} className='mx-1'>
                                <FormGroup>
                                    <Label for="patientId">Patient ID:</Label>
                                    <Input
                                        type="text"
                                        id="patientId"
                                        value={patientId}
                                        onChange={(e) => this.handleChange('patientId', e.target.value)}
                                    />
                                </FormGroup>
                            </Col>
                        </Row>
                        <Row form className='align-items-center mt-3 my-0'>
                            <Col md={2}>
                                <FormGroup>
                                    <Label for="rowsPerPage">Rows per Page:</Label>
                                    <Select
                                        id="rowsPerPage"
                                        value={rowsPerPage}
                                        onChange={(e) => this.handleChange('rowsPerPage', e.target.value)}
                                        displayEmpty
                                        fullWidth
                                    >
                                        {[10, 20, 30, 40, 50, 60].map(number => (
                                            <MenuItem key={number} value={number}>
                                                {number}
                                            </MenuItem>
                                        ))}
                                    </Select>
                                </FormGroup>
                            </Col>
                            <Col md={2}>
                                <FormGroup>
                                    <Label for="tableWidth">Table Width (px):</Label>
                                    <TextField
                                        id="tableWidth"
                                        type="number"
                                        value={tableWidth}
                                        inputProps={{
                                            step: "50"
                                        }}
                                        onChange={(e) => this.handleChange('tableWidth', e.target.value)}
                                        fullWidth
                                    />
                                </FormGroup>
                            </Col>
                            <Col md={2}>
                                <FormGroup>
                                    <Label for="days">Days:</Label>
                                    <Input
                                        type="number"
                                        id="days"
                                        value={days}
                                        onChange={(e) => this.handleChange('days', e.target.value)}
                                    />
                                    {!isDaysValid && <div style={{ color: 'red' }}>Please enter a valid number of days.</div>}
                                </FormGroup>
                            </Col>
                        </Row>
                        <Row form className='align-items-center mt-3'>
                            <Col md={6}>
                                <Button
                                    color="primary"
                                    onClick={() => this.handleSubmit(false)}
                                    className="mt-1"
                                    disabled={!isDaysValid}
                                    style={{ marginRight: '10px' }}
                                >
                                    Daily Exams
                                </Button>
                                <Button
                                    color="secondary"
                                    onClick={() => this.handleSubmit(true)}
                                    className="mt-1"
                                    disabled={!isDaysValid}
                                >
                                    Summarized Exams ( per days )
                                </Button>
                            </Col>
                        </Row>
                    </Form>

                    {/* Display no exams message if no exams are found */}
                    {noExamsMessage && (
                        <div className="alert alert-info mt-4">
                            No exams found for the selected date range and patient.
                        </div>
                    )}

                    {/* Render Table if Data is Fetched */}
                    {showTable && !noExamsMessage && (
                        <div className="table-responsive" style={{ width: `${tableWidth}px` }}>
                            <TableContainer component={Paper}>
                                <Table size="small">
                                    <TableHead>
                                        <TableRow>
                                            {columns.map((column, index) => (
                                                <TableCell key={index}>
                                                    {column.Header}
                                                </TableCell>
                                            ))}
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {data.map((row, rowIndex) => (
                                            <TableRow key={rowIndex}>
                                                {columns.map((column, colIndex) => (
                                                    <TableCell
                                                        key={colIndex}
                                                        style={colIndex === 0 ? { position: 'sticky', left: 0, backgroundColor: 'white', zIndex: "auto" } : {}}
                                                    >
                                                        {row[column.accessor]}
                                                    </TableCell>
                                                ))}
                                            </TableRow>
                                        ))}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        </div>
                    )}

                    {/* Render Pagination if there are Multiple Pages */}
                    {showTable && totalPages > 1 && (
                        <div className="pagination">
                            <Pagination
                                count={totalPages}
                                page={currentPage + 1}
                                onChange={this.handlePageChange}
                                variant="outlined"
                                shape="rounded"
                            />
                        </div>
                    )}
                </Container>
            </div>
        );
    }
}

export default PatientExams;

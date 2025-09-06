import React, { Component } from 'react';
import { Container, Row, Col, Button } from 'reactstrap';
import { Link, withRouter } from 'react-router-dom';
import AppNavbar from './AppNavbar';

class PatientProfileEdit extends Component {
    state = {
        allFields: [], // Holds the available fields not yet selected
        rightItems: [], // Holds the fields that have been selected for the patient profile
        selectedLeftItems: [], // Holds the currently selected items from the left list (allFields)
        selectedRightItems: [], // Holds the currently selected items from the right list (rightItems)
    };

    componentDidMount() {
        this.fetchFields(); // Fetch the fields data when the component mounts
    }

    // Function to fetch fields data from the server
    fetchFields = async () => {
        try {
            // Helper function to fetch JSON data from a given URL
            const fetchJson = async (url) => {
                const response = await fetch(url);
                return response.json();
            };

            // Fetch data from multiple endpoints in parallel
            const [allFieldsJSON, groupedFieldsJSON, selectedFieldsJSON, groupFieldsJSON] = await Promise.all([
                fetchJson('/fields/get-all-unique'), // Fetch all unique fields
                fetchJson(`/group_fields/get-profile-grouped?patient_id=${this.props.match.params.id}`), // Fetch grouped fields for the patient
                fetchJson(`/patient_profile/get-profile?patient_id=${this.props.match.params.id}`), // Fetch selected fields for the patient
                fetchJson('/group_fields/get-all') // Fetch all group fields
            ]);

            // Format group fields data
            const groupFieldsFormat = (item) => {
                return {
                    field: {
                        field_id: item.field.field_id,
                        description_gr: item.field.description_gr,
                        description_en: item.field.description_en,
                        frequency: item.field.frequency,
                        measurement_unit: item.field.measurement_unit,
                        acceptable_range: item.field.acceptable_range,
                        group_id: item.id.group_id,
                    },
                    required: item.required || 0,
                    guideline: item.guideline || null,
                };
            };

            // Map data to a new format for consistency
            const mapToNewFormat = (item) => {
                return {
                    field: {
                        field_id: item.field_id || (item.field && item.field.field_id),
                        description_gr: item.description_gr || (item.field && item.field.description_gr),
                        description_en: item.description_en || (item.field && item.field.description_en),
                        frequency: item.frequency || (item.field && item.field.frequency),
                        measurement_unit: item.measurement_unit || (item.field && item.field.measurement_unit),
                        acceptable_range: item.acceptable_range || (item.field && item.field.acceptable_range),
                        group_id: null,
                    },
                    required: item.required || 0,
                    guideline: item.guideline || null,
                };
            };

            const allFieldsFormatted = allFieldsJSON.map(mapToNewFormat);
            const selectedFieldsFormatted = selectedFieldsJSON.map(mapToNewFormat);
            const groupedFieldsFormatted = groupFieldsJSON.map(groupFieldsFormat);
            const groupedFieldsJSONFormatted = groupedFieldsJSON.map(groupFieldsFormat);

            console.log('allFieldsFormatted:', allFieldsFormatted);
            console.log('selectedFieldsFormatted:', selectedFieldsFormatted);
            console.log('groupedFieldsFormatted:', groupedFieldsFormatted);
            console.log('groupedFieldsJSONFormatted:', groupedFieldsJSONFormatted);

            // Update selected fields based on grouped fields
            const updatedSelectedFields = selectedFieldsFormatted.map(selectedField => {
                const correspondingGroupedField = groupedFieldsJSONFormatted.find(groupedField =>
                    groupedField.field.field_id === selectedField.field.field_id
                );

                // If corresponding grouped field exists, update it
                if (correspondingGroupedField && correspondingGroupedField.required !== selectedField.required) {
                    correspondingGroupedField.required = selectedField.required;
                    if (correspondingGroupedField && correspondingGroupedField.guideline === null && correspondingGroupedField.guideline !== selectedField.guideline) {
                        correspondingGroupedField.guideline = selectedField.guideline;
                    }
                    return null; // Mark for deletion
                }
                else if (correspondingGroupedField && correspondingGroupedField.guideline === null && correspondingGroupedField.guideline !== selectedField.guideline) {
                    correspondingGroupedField.guideline = selectedField.guideline;
                    return null; // Mark for deletion
                }
                else if (correspondingGroupedField && selectedField.field.group_id === null) {
                    return null; // Mark for deletion
                }
                return selectedField; // Return original if no updates are necessary
            }).filter(Boolean); // Remove null items (marked for deletion)

            console.log('updatedSelectedFields:', updatedSelectedFields);

            // Combine updated selected fields and grouped fields
            const finalSelectedFields = [...updatedSelectedFields, ...groupedFieldsJSONFormatted];

            // Filter available group fields
            const availableGroupFields = groupedFieldsFormatted.filter(item =>
                !finalSelectedFields.some(selectedItem => selectedItem.field.group_id === item.field.group_id)
            );

            // Filter available ungrouped fields
            const availableUngroupedFields = allFieldsFormatted.filter(item =>
                !finalSelectedFields.some(selectedItem => selectedItem.field.field_id === item.field.field_id)
            );

            const availableFields = [...availableGroupFields, ...availableUngroupedFields];

            this.setState({
                allFields: availableFields, // Update state with available fields
                rightItems: finalSelectedFields, // Update state with selected fields
            });
        } catch (error) {
            console.error('Error fetching patient profiles:', error);
        }
    };

    // Function to move items between lists
    moveItems = (sourceKey, destinationKey, selectedSourceItemsKey, selectedDestinationItemsKey, mapper) => {
        const { [sourceKey]: source, [destinationKey]: destination, [selectedSourceItemsKey]: selectedSourceItems } = this.state;

        if (selectedSourceItems.length > 0) {
            const existingFieldIds = new Set(destination.map(item => mapper(item)));
            const newDestinationItems = selectedSourceItems
                .filter(selectedItem => !existingFieldIds.has(mapper(selectedItem)))
                .map(selectedItem => ({
                    field: selectedItem.field,
                    required: selectedItem.required || 0,
                    guideline: selectedItem.guideline || null,
                }));

            const remainingItems = source.filter(item => !selectedSourceItems.includes(item) && !newDestinationItems.some(newItem => mapper(newItem) === mapper(item)));

            this.setState({
                [sourceKey]: remainingItems, // Update source list
                [destinationKey]: [...destination, ...newDestinationItems], // Update destination list
                [selectedSourceItemsKey]: [], // Clear selected source items
            });
        }
    };

    moveLeftToRight = () => {
        this.moveItems('allFields', 'rightItems', 'selectedLeftItems', 'selectedRightItems', item => item.field.field_id);
    };

    moveRightToLeft = () => {
        this.moveItems('rightItems', 'allFields', 'selectedRightItems', 'selectedLeftItems', item => item.field.field_id);
    };

    render() {
        const { allFields, rightItems, selectedLeftItems, selectedRightItems } = this.state;

        console.log('allFields:', allFields);
        console.log('rightItems:', rightItems);
        console.log('selectedLeftItems:', selectedLeftItems);
        console.log('selectedRightItems:', selectedRightItems);

        return (
            <div>
                <AppNavbar />
                <Container>
                    <Row>
                        <ProfileEditListBox
                            items={allFields}
                            selectedItems={selectedLeftItems}
                            onItemSelected={items => this.setState({ selectedLeftItems: items })}
                            onItemMoved={this.moveLeftToRight}
                            title="Available Fields"
                            buttonText="Add >>"
                        />
                        <ProfileEditListBox
                            items={rightItems}
                            selectedItems={selectedRightItems}
                            onItemSelected={items => this.setState({ selectedRightItems: items })}
                            onItemMoved={this.moveRightToLeft}
                            title="Selected Fields"
                            buttonText="<< Remove"
                        />
                    </Row>
                    <Row>
                        <Col>
                            <Button className="mt-5" color="primary" onClick={this.handleSave}>
                                Save
                            </Button>{' '}
                            <Button className="mt-5" color="secondary" tag={Link} to="/patients" onClick={this.handleCancel}>
                                Cancel
                            </Button>
                        </Col>
                    </Row>
                </Container>
            </div>
        );
    }

    // Handle save button click
    handleSave = async () => {
        const { rightItems } = this.state;

        console.log('Request Payload:', JSON.stringify(rightItems));

        const url = `/patient_profile/save-profile?patient_id=${this.props.match.params.id}`;

        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(rightItems),
        });

        if (response.ok) {
            console.log('Save successful');
            window.location.reload();
        } else {
            console.error('Save failed');
        }
    };

    // Handle cancel button click
    handleCancel = () => {
        // Handle cancel logic here
    };
}

// Component for displaying list boxes with fields
const ProfileEditListBox = ({ items = [], selectedItems = [], onItemSelected, onItemMoved, title, buttonText }) => {
    const renderSelectedItems = () => {
        return selectedItems.map((selectedItem, index) => (
            <div key={index} style={{ marginBottom: '10px' }} className="selected-item">
                {/* Display the name of the selected item with bold title */}
                <p style={{ fontWeight: 'bold', marginBottom: '5px' }}>{selectedItem.field.description_gr}</p>
                <div style={{ marginLeft: '10px' }}>
                    <label>Guideline:</label>
                    <input
                        type="text"
                        value={selectedItem.guideline || ''}
                        onChange={e => {
                            const updatedItems = [...selectedItems];
                            updatedItems[index].guideline = e.target.value;
                            onItemSelected(updatedItems);
                        }}
                    />
                    <label className="ml-2">Required:</label>
                    <select
                        value={selectedItem.required || 0}
                        onChange={e => {
                            const updatedItems = [...selectedItems];
                            updatedItems[index].required = parseInt(e.target.value);
                            onItemSelected(updatedItems);
                        }}
                    >
                        <option value={0}>Optional</option>
                        <option value={1}>Mandatory</option>
                    </select>
                </div>
            </div>
        ));
    };

    return (
        <Col>
            <h3>{title}</h3>
            <select
                className="form-select"
                size="20"
                multiple={true}
                value={selectedItems.map(item => `${item.field.field_id}-${item.field.group_id}`)}
                onChange={e => {
                    const selectedIds = Array.from(e.target.selectedOptions, option => option.value);
                    // Handle selection logic when selecting items from the list
                    if (selectedIds.length > 0 && !selectedIds[0].toString().endsWith(null)) {
                        const clickedItem = items.find(item => `${item.field.field_id}-${item.field.group_id}` === selectedIds[0]);

                        if (clickedItem) {
                            const itemsToSelect = items.filter(item =>
                                item.field.group_id === clickedItem.field.group_id
                            );
                            onItemSelected(itemsToSelect);
                        }
                    } else if (selectedIds[0].toString().endsWith(null)) {
                        const clickedItem = items.find(item => `${item.field.field_id}-${item.field.group_id}` === selectedIds[0]);
                        if (clickedItem) {
                            const itemsToSelect = items.filter(item =>
                                item.field.field_id === clickedItem.field.field_id
                            );
                            onItemSelected(itemsToSelect);
                        }
                    }
                }}
            >
                {items.map(item => (
                    <option
                        key={`${item.field.field_id}-${item.field.group_id}`}
                        value={`${item.field.field_id}-${item.field.group_id}`}
                    >
                        {item.field.description_gr}
                    </option>
                ))}
            </select>
            <div className="mt-2">{renderSelectedItems()}</div>
            <Button size="sm" className="mt-sm-2" color="primary" onClick={onItemMoved}>
                {buttonText}
            </Button>
        </Col>
    );
};

export default withRouter(PatientProfileEdit);

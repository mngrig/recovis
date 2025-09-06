import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';

class PatientList extends Component {

    constructor(props) {
        super(props);
        this.state = {patients: []};
    }

    componentDidMount() {
        fetch('/patient/get-all')
            .then(response => response.json())
            .then(data => this.setState({patients: data}));
    }

    render() {
        const {patients, isLoading} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        const patientList = patients.map(patient => {
            return <tr key={patient.patient_id}>
                <td style={{whiteSpace: 'nowrap'}}>{patient.first_name}</td>
                <td>{patient.second_name}</td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="primary" tag={Link} to={"/patients/edit_profile/" + patient.patient_id}>Edit Profile</Button>
                        <Button size="sm" color="secondary" tag={Link} to={"/patients/" + patient.patient_id}>Edit Patient</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <div className="float-end">
                        <Button color="success" tag={Link} to="/patients/new">Add Patient</Button>
                    </div>
                    <h3>Patients</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="30%">First Name</th>
                            <th width="30%">Second Name</th>
                            <th width="50%">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {patientList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );
    }
}
export default PatientList;
import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from './AppNavbar';

class PatientEdit extends Component {

    emptyItem = {
        patient_id: null,
        first_name: null,
        second_name: null,
        tel: null,
        email: null,
        last_transplant_date: null,
        transplant_type: null,
        transplants_num: null,
        kidney_failure_cause: null,
        username: null,
        userpassword: null
    };

    constructor(props) {
        super(props);
        this.state = {
            item: this.emptyItem
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    async componentDidMount() {
        if (this.props.match.params.id !== 'new') {
            const patient = await (await fetch(`/patient/get-patient?patient_id=${this.props.match.params.id}`)).json();
            this.setState({ item: patient || this.emptyItem });
        }
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let item = { ...this.state.item };
        item[name] = value;
        this.setState({ item });
    }

    async handleSubmit(event) {
        event.preventDefault();
        const { item } = this.state;

        console.log('Request Payload:', JSON.stringify(item));

        const url = '/patient/save-patient';

        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item),
        });

        if (response.ok) {
            this.props.history.push('/patients');
        } else {
            // Handle errors
            console.error('Failed to save patient');

            // Log the response status and any error messages
            console.error('Response Status:', response.status);
            const errorBody = await response.json(); // assuming the error response is in JSON format
            console.error('Error Body:', errorBody);
        }
    }

    render() {
        const { item } = this.state;
        let title;

        if(this.props.match.params.id !== 'new')
        {
            title = <h2> Edit Patient </h2>;
        }
        else {
            title = <h2> New Patient </h2>;
        }

        return <div>
            <AppNavbar />
            <Container>
                {title}
                <Form onSubmit={this.handleSubmit}>
                    <FormGroup>
                        <Label for="patient_id">Patient ID</Label>
                        <Input type="text"
                               name="patient_id"
                               id="patient_id"
                               value={item.patient_id || ''}
                               onChange={this.handleChange}
                               autoComplete="patient_id"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="first_name">First Name</Label>
                        <Input type="text"
                               name="first_name"
                               id="first_name"
                               value={item.first_name || ''}
                               onChange={this.handleChange}
                               autoComplete="first_name"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="second_name">Second Name</Label>
                        <Input type="text"
                               name="second_name"
                               id="second_name"
                               value={item.second_name || ''}
                               onChange={this.handleChange}
                               autoComplete="second_name"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="date_of_birth">Date of Birth</Label>
                        <Input type="date"
                               name="date_of_birth"
                               id="date_of_birth"
                               value={item.date_of_birth || ''}
                               onChange={this.handleChange} autoComplete="date_of_birth"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="email">Email</Label>
                        <Input type="text"
                               name="email"
                               id="email"
                               value={item.email || ''}
                               onChange={this.handleChange}
                               autoComplete="email"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="last_transplant_date">Last Transplant Date</Label>
                        <Input type="date"
                               name="last_transplant_date"
                               id="last_transplant_date"
                               value={item.last_transplant_date || ''}
                               onChange={this.handleChange} autoComplete="last_transplant_date"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="transplant_type">Transplant Type</Label>
                        <select
                            name="transplant_type"
                            id="transplant_type"
                            value={item.transplant_type || ''}
                            onChange={this.handleChange}
                            autoComplete="transplant_type">
                            <option value="alive">Alive</option>
                            <option value="dead">Dead</option>
                        </select>
                    </FormGroup>

                    <FormGroup>
                        <Label for="transplants_num">Number of Transplants</Label>
                        <Input type="number"
                               name="transplants_num"
                               id="transplants_num"
                               value={item.transplants_num || ''}
                               onChange={this.handleChange}
                               autoComplete="transplants_num"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="kidney_failure_cause">Kidney Failure Cause</Label>
                        <Input type="text"
                               name="kidney_failure_cause"
                               id="kidney_failure_cause"
                               value={item.kidney_failure_cause || ''}
                               onChange={this.handleChange}
                               autoComplete="kidney_failure_cause"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="username">Username</Label>
                        <Input type="text"
                               name="username"
                               id="username"
                               value={item.username || ''}
                               onChange={this.handleChange}
                               autoComplete="username"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="userpassword">Password</Label>
                        <Input type="password"
                               name="userpassword"
                               id="userpassword"
                               value={item.userpassword || ''}
                               onChange={this.handleChange}
                               autoComplete="userpassword"/>
                    </FormGroup>

                    <FormGroup>
                        <Button color="primary" type="submit">Save</Button>{' '}
                        <Button color="secondary" tag={Link} to="/patients">Cancel</Button>
                    </FormGroup>
                </Form>
            </Container>
        </div>
    }
}
export default withRouter(PatientEdit);
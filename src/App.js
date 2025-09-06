import React, { Component } from 'react';
import './App.css';
import Home from './Home';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import PatientList from './PatientList';
import PatientEdit from './PatientEdit';
import PatientProfileEdit from './PatientProfileEdit';
import PatientExams from "./PatientExams";
import Chart from "./Chart";

class App extends Component {
  render() {
    return (
        <Router>
          <Switch>
            <Route path='/' exact={true} component={Home}/>
            <Route path='/patients' exact={true} component={PatientList}/>
              <Route path='/patients/edit_profile/:id' component={PatientProfileEdit}/>
              <Route path='/patients/:id' component={PatientEdit}/>
            <Route path='/exams' exact={true} component={PatientExams}/>
            <Route path='/charts' exact={true} component={Chart}/>
          </Switch>
        </Router>
    )
  }
}
export default App;

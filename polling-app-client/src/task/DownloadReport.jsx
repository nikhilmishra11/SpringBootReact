import React, { Component } from 'react';
import { createPoll } from '../util/APIUtils';
import { createReport } from '../util/APIUtils';
import { downloadReport } from '../util/APIUtils';
import { MAX_CHOICES, POLL_QUESTION_MAX_LENGTH, POLL_CHOICE_MAX_LENGTH } from '../constants';
import './DownloadReport.css';
import { Form, Input, Button, Icon, Select, Col, notification } from 'antd';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
const Option = Select.Option;
const FormItem = Form.Item;
const { TextArea } = Input

class DownloadReport extends Component {
    constructor(props) {
        super(props);
        this.state = {
      

        };
        
        
        this.handleSubmit = this.handleSubmit.bind(this);
        
        //this.isFormInvalid = this.isFormInvalid.bind(this);
    }

    

    handleSubmit(event) {
        console.log('inside handle submit event');
        event.preventDefault();
        // const pollData = {
        //     question: this.state.question.text,
        //     choices: this.state.choices.map(choice => {
        //         return { text: choice.text }
        //     }),
        //     pollLength: this.state.pollLength
        // };

                   
        downloadReport()
            .then(response => {
                this.props.history.push("/");
            }).catch(error => {
                if (error.status === 401) {
                    this.props.handleLogout('/login', 'error', 'You have been logged out. Please login create poll.');
                } else {
                    notification.error({
                        message: 'WSR App',
                        description: error.message || 'Sorry! Something went wrong. Please try again!'
                    });
                }
            });


    }

    

    render() {
        const choiceViews = [];
        // this.state.choices.forEach((choice, index) => {
        //     choiceViews.push(<PollChoice key={index} choice={choice} choiceNumber={index} removeChoice={this.removeChoice} handleChoiceChange={this.handleChoiceChange} />);
        // });

        return (
            <div className="new-poll-container">
                <h1 className="page-title">Download Report</h1>
                <div className="new-poll-content">
                    <Form onSubmit={this.handleSubmit} className="create-poll-form">
                        <FormItem className="poll-form-row">
                            <Button type="primary"
                                htmlType="submit"
                                size="large"
                                //disabled={this.isFormInvalid()}
                                className="create-poll-form-button">Download Report</Button>
                        </FormItem>
                    </Form>
                </div>
            </div>
        );
    }
}

function PollChoice(props) {
    return (
        <FormItem validateStatus={props.choice.validateStatus}
            help={props.choice.errorMsg} className="poll-form-row">
            <Input
                placeholder={'Choice ' + (props.choiceNumber + 1)}
                size="large"
                value={props.choice.text}
                className={props.choiceNumber > 1 ? "optional-choice" : null}
                onChange={(event) => props.handleChoiceChange(event, props.choiceNumber)} />

            {
                props.choiceNumber > 1 ? (
                    <Icon
                        className="dynamic-delete-button"
                        type="close"
                        disabled={props.choiceNumber <= 1}
                        onClick={() => props.removeChoice(props.choiceNumber)}
                    />) : null
            }
        </FormItem>
    );
}


export default DownloadReport;
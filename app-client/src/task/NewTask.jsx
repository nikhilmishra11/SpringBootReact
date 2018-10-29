import React, { Component } from 'react';
import { createReport } from '../util/APIUtils';
import { POLL_QUESTION_MAX_LENGTH, POLL_CHOICE_MAX_LENGTH } from '../constants';
import './NewTask.css';
import { Form, Input, Button, Icon, Select, Col, notification } from 'antd';
import Datetime from 'react-datetime';
import 'react-datetime/css/react-datetime.css';
const Option = Select.Option;
const FormItem = Form.Item;
const { TextArea } = Input

class NewTask extends Component {
    constructor(props) {
        super(props);
        this.state = {
            question: {
                text: ''
            },
            choices: [{
                text: ''
            }, {
                text: ''
            }],
            pollLength: {
                days: 1,
                hours: 0
            },
            keyUpdateValue: {
                text: ''
            },
            owner: {
                text: ''
            },
            task: {
                text: ''
            },
            actualStartDate: {
                text: ''
            },
            actualEndDate: {
                text: ''
            },
            status: {
                text: ''
            },
            remarks: {
                text: ''
            }

        };
        this.addChoice = this.addChoice.bind(this);
        this.removeChoice = this.removeChoice.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleQuestionChange = this.handleQuestionChange.bind(this);
        this.handleKeyUpdateChange = this.handleKeyUpdateChange.bind(this);
        this.handleTaskChange = this.handleTaskChange.bind(this);
        this.handleActualStartDateChange = this.handleActualStartDateChange.bind(this);
        this.handleActualEndDateChange = this.handleActualEndDateChange.bind(this);
        this.handleStatusChange = this.handleStatusChange.bind(this);
        this.handleRemarksChange = this.handleRemarksChange.bind(this);
        this.handleChoiceChange = this.handleChoiceChange.bind(this);
        this.handlePollDaysChange = this.handlePollDaysChange.bind(this);
        this.handlePollHoursChange = this.handlePollHoursChange.bind(this);
        this.isFormInvalid = this.isFormInvalid.bind(this);
    }

    addChoice(event) {
        const choices = this.state.choices.slice();
        this.setState({
            choices: choices.concat([{
                text: ''
            }])
        });
    }

    removeChoice(choiceNumber) {
        const choices = this.state.choices.slice();
        this.setState({
            choices: [...choices.slice(0, choiceNumber), ...choices.slice(choiceNumber + 1)]
        });
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

        const reportData = {
            keyUpdateValue: this.state.keyUpdateValue.text,
            owner: this.state.owner.text,
            task: this.state.task.text,
            actualStartDate: this.state.actualStartDate.text,
            actualEndDate: this.state.actualEndDate.text,
            status: this.state.status.text,
            remarks: this.state.remarks.text
            
        };

       
        console.log(reportData);
            
        createReport(reportData)
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

    validateInput = (inputText) => {
        if (inputText.length === 0) {
            return {
                validateStatus: 'error',
                errorMsg: 'Please enter your question!'
            }
        } else if (inputText.length > POLL_QUESTION_MAX_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Question is too long (Maximum ${POLL_QUESTION_MAX_LENGTH} characters allowed)`
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        }
    }


    validateQuestion = (questionText) => {
        if (questionText.length === 0) {
            return {
                validateStatus: 'error',
                errorMsg: 'Please enter your question!'
            }
        } else if (questionText.length > POLL_QUESTION_MAX_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Question is too long (Maximum ${POLL_QUESTION_MAX_LENGTH} characters allowed)`
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        }
    }

    validateKeyUpdate = (keyUpdateValueText) => {
        if (keyUpdateValueText.length === 0) {
            return {
                validateStatus: 'error',
                errorMsg: 'Please enter your question!'
            }
        } else if (keyUpdateValueText.length > POLL_QUESTION_MAX_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Question is too long (Maximum ${POLL_QUESTION_MAX_LENGTH} characters allowed)`
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        }
    }

    handleInputChange(event) {
        const value = event.target.value;
        const name = event.target.name;
        this.setState({
            name: {
                text: value,
                ...this.validateInput(value)
            }
        });
    }

    handleQuestionChange(event) {
        const value = event.target.value;
        this.setState({
            question: {
                text: value,
                ...this.validateQuestion(value)
            }
        });
    }

    handleKeyUpdateChange(event) {
        const value = event.target.value;
        this.setState({
            keyUpdateValue: {
                text: value,
                ...this.validateQuestion(value)
            }
        });
    }

    handleTaskChange(event) {
        const value = event.target.value;
        this.setState({
            task: {
                text: value,
                ...this.validateQuestion(value)
            }
        });
    }

    handleActualStartDateChange(date) {
        this.setState({
            actualStartDate: {
                text: date._d.valueOf(),
            }
        });
    }


    handleActualEndDateChange(date) {
        this.setState({
            actualEndDate: {
                text: date._d.valueOf(),
            }
        });
    }


    handleStatusChange(event) {
        const value = event.target.value;
        this.setState({
            status: {
                text: value,
                ...this.validateQuestion(value)
            }
        });
    }


    handleRemarksChange(event) {
        const value = event.target.value;
        this.setState({
            remarks: {
                text: value,
                ...this.validateQuestion(value)
            }
        });
    }





    validateChoice = (choiceText) => {
        if (choiceText.length === 0) {
            return {
                validateStatus: 'error',
                errorMsg: 'Please enter a choice!'
            }
        } else if (choiceText.length > POLL_CHOICE_MAX_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Choice is too long (Maximum ${POLL_CHOICE_MAX_LENGTH} characters allowed)`
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        }
    }

    handleChoiceChange(event, index) {
        const choices = this.state.choices.slice();
        const value = event.target.value;

        choices[index] = {
            text: value,
            ...this.validateChoice(value)
        }

        this.setState({
            choices: choices
        });
    }


    handlePollDaysChange(value) {
        const pollLength = Object.assign(this.state.pollLength, { days: value });
        this.setState({
            pollLength: pollLength
        });
    }

    handlePollHoursChange(value) {
        const pollLength = Object.assign(this.state.pollLength, { hours: value });
        this.setState({
            pollLength: pollLength
        });
    }

    isFormInvalid() {
        if (this.state.question.validateStatus !== 'success') {
            return true;
        }

        for (let i = 0; i < this.state.choices.length; i++) {
            const choice = this.state.choices[i];
            if (choice.validateStatus !== 'success') {
                return true;
            }
        }
    }

    render() {
        const choiceViews = [];
        // this.state.choices.forEach((choice, index) => {
        //     choiceViews.push(<PollChoice key={index} choice={choice} choiceNumber={index} removeChoice={this.removeChoice} handleChoiceChange={this.handleChoiceChange} />);
        // });

        return (
            <div className="new-poll-container">
                <h1 className="page-title">Create Report</h1>
                <div className="new-poll-content">
                    <Form onSubmit={this.handleSubmit} className="create-poll-form">
                    
                        <FormItem validateStatus={this.state.keyUpdateValue.validateStatus}
                            help={this.state.keyUpdateValue.errorMsg} className="poll-form-row">
                            <TextArea
                                placeholder="Enter Key Updates "
                                style={{ fontSize: '16px' }}
                                autosize={{ minRows: 3, maxRows: 6 }}
                                name="keyUpdateValue"
                                value={this.state.keyUpdateValue.text}
                                onChange = {this.handleKeyUpdateChange}
                                 />
                        </FormItem>
                        <FormItem validateStatus={this.state.task.validateStatus}
                            help={this.state.task.errorMsg} className="poll-form-row">
                            <TextArea
                                placeholder="Enter Task "
                                style={{ fontSize: '16px' }}
                                autosize={{ minRows: 3, maxRows: 6 }}
                                name="task"
                                value={this.state.task.text}
                                onChange = {this.handleTaskChange}
                                />
                        </FormItem>
                        {choiceViews}
                        <FormItem validateStatus={this.state.actualStartDate.validateStatus}
                            help={this.state.actualStartDate.errorMsg} className="poll-form-row">
                            <Datetime 
                                inputProps={{ placeholder: 'Enter Actual Start Date', readonly: 'true' }}
                                dateFormat="Do MMM YYYY"
                                timeFormat={false}
                                className="poll-form-row wsr-date-picker"
                                value={this.state.actualStartDate.text}
                                onChange = {this.handleActualStartDateChange}
                                closeOnSelect={true}
                                />
                            {/* <TextArea
                                placeholder="Enter Actual Start Date "
                                style={{ fontSize: '16px' }}
                                autosize={{ minRows: 3, maxRows: 6 }}
                                name="actualStartDate"
                                value={this.state.actualStartDate.text}
                                onChange = {this.handleActualStartDateChange}
                                 /> */}
                        </FormItem>
                        <FormItem validateStatus={this.state.actualEndDate.validateStatus}
                            help={this.state.actualEndDate.errorMsg} className="poll-form-row">
                            <Datetime 
                                inputProps={{ placeholder: 'Enter Actual End Date', readonly: 'true' }}
                                dateFormat="Do MMM YYYY"
                                timeFormat={false}
                                className="poll-form-row wsr-date-picker"
                                value={this.state.actualEndDate.text}
                                onChange = {this.handleActualEndDateChange}
                                closeOnSelect={true}
                                />
                            {/* <TextArea
                                placeholder="Enter Actual End Date "
                                style={{ fontSize: '16px' }}
                                autosize={{ minRows: 3, maxRows: 6 }}
                                name="actualEndDate"
                                value={this.state.actualEndDate.text}
                                onChange = {this.handleActualEndDateChange}
                                 /> */}
                        </FormItem>
                        <FormItem validateStatus={this.state.status.validateStatus}
                            help={this.state.status.errorMsg} className="poll-form-row">
                            <TextArea
                                placeholder="Enter your  Status "
                                style={{ fontSize: '16px' }}
                                autosize={{ minRows: 3, maxRows: 6 }}
                                name="status"
                                value={this.state.status.text}
                                onChange = {this.handleStatusChange}
                                 />
                        </FormItem>
                        <FormItem validateStatus={this.state.remarks.validateStatus}
                            help={this.state.remarks.errorMsg} className="poll-form-row">
                            <TextArea
                                placeholder="Enter Remarks, if any "
                                style={{ fontSize: '16px' }}
                                autosize={{ minRows: 3, maxRows: 6 }}
                                name="remarks"
                                value={this.state.remarks.text}
                                onChange = {this.handleRemarksChange}
                                />
                        </FormItem>
                        
                        <FormItem className="poll-form-row">
                            <Button type="primary"
                                htmlType="submit"
                                size="large"
                                //disabled={this.isFormInvalid()}
                                className="create-poll-form-button">Create Report</Button>
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


export default NewTask;
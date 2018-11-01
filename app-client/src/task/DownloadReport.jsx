import React, { Component } from 'react';
import { downloadReport, getFileNameByContentDisposition } from '../util/APIUtils';
import './DownloadReport.css';
import { Form, Input, Button, Icon, Select,  notification } from 'antd';
import 'react-datepicker/dist/react-datepicker.css';
import Datetime from 'react-datetime';
const Option = Select.Option;
const FormItem = Form.Item;
const { TextArea } = Input

class DownloadReport extends Component {
    constructor(props) {
        super(props);
        this.state = {
            actualStartDate: {
                text: ''
            },
            actualEndDate: {
                text: ''
            },
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

        let fileName = 'wsrautomator_generated.pptx';
        downloadReport()
            .then(response => {
                fileName = getFileNameByContentDisposition(response.headers.get('Content-Disposition'));
                return response.blob();
            })
            .then(body => {
                const blob = new Blob([body], { type: 'application/octet-stream' });
                //const blob = new File([response], "filename.pptx", {type: 'application/octet-stream'});
                const url = window.URL.createObjectURL(blob);
                const anchor = document.createElement('a');
                anchor.download = '' + fileName;
                anchor.href = url;
                anchor.click();
            }).catch(error => {
                console.log(error);
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
                    <FormItem validateStatus={this.state.actualStartDate.validateStatus}
                        help={this.state.actualStartDate.errorMsg} className="poll-form-row">
                        <Datetime
                            inputProps={{ placeholder: 'From Date', readonly: 'true' }}
                            dateFormat="Do MMM YYYY"
                            timeFormat={false}
                            className="poll-form-row wsr-date-picker"
                            value={this.state.actualStartDate.text}
                            onChange={this.handleActualStartDateChange}
                            closeOnSelect={true}
                        />
                    </FormItem>
                    <FormItem validateStatus={this.state.actualEndDate.validateStatus}
                        help={this.state.actualEndDate.errorMsg} className="poll-form-row">
                        <Datetime
                            inputProps={{ placeholder: 'To Date', readonly: 'true' }}
                            dateFormat="Do MMM YYYY"
                            timeFormat={false}
                            className="poll-form-row wsr-date-picker"
                            value={this.state.actualEndDate.text}
                            onChange={this.handleActualEndDateChange}
                            closeOnSelect={true}
                        />
                    </FormItem>
                    <Form onSubmit={this.handleSubmit} className="create-poll-form">
                        <FormItem className="poll-form-row">
                            <Button type="primary"
                                htmlType="submit"
                                size="large"
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
import React from 'react';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import { Validation } from '../../utility/models/validation/Validation';

interface DropdownProps {
    children?: React.ReactNode;
    options: Array<{ label: string | number; value: string }>;
    name?: string,
    value?: {},
    label: string,
    isRequired?: boolean,
    onChange: (e: React.ChangeEvent<HTMLSelectElement>) => void,
    feedback?:Validation,
    setFeedback?:any
}

function dropdownSelection({ options, onChange, name, value, label, isRequired, feedback, setFeedback }: DropdownProps) {

    return (
        <>
            <Form.Group as={Row} controlId={name} className='mb-3'>
                <Form.Label column sm={12} className='text-start text-nowrap'>{label}</Form.Label>
                <Col sm={12}>
                    <Form.Select isInvalid={feedback?.isValid && name===feedback.inputFieldName}  name={name} value={value !== undefined ? String(value) : ''} aria-label="Default select example" onChange={onChange}>
                        <option value="" disabled>Choose </option>
                        {options.map(option => (
                            <option key={option.value} value={option.value}>{option.label}</option>
                        ))}
                    </Form.Select>
                    {name === feedback?.inputFieldName && <Form.Control.Feedback type='invalid'>{feedback?.errorMessage}</Form.Control.Feedback>}
                </Col>
            </Form.Group>
        </>
    )
}

export default dropdownSelection
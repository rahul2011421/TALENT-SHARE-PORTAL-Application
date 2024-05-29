import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import { Validation } from '../../utility/models/validation/Validation';
import './input.css'

interface InputProps {
  label: string;
  name: string;
  type: string;
  value: string;
  feedback?: Validation;
  isRequired?: boolean,
  setFeedback?: any,
  onChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  readOnly?: boolean;
}


function Input({ label, name, type, value, onChange, feedback, setFeedback, isRequired, readOnly }: InputProps) {

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (setFeedback) {
      setFeedback({ ...feedback, isValid: false, inputFieldName: e.target.name, errorMessage: "" })
    }
    onChange(e);
  };

  return (
    <>
      <Form.Group as={Row} controlId={name} className='mb-3' onChange={handleChange}>
        <Form.Label column sm={12} className='text-start text-nowrap input_label_styles'>{label}</Form.Label>
        <Col sm={12}>
          <Form.Control
            type={type}
            name={name}
            value={value}
            onChange={onChange}
            placeholder={`Enter ${label}`}
            isInvalid={feedback?.isValid && name === feedback.inputFieldName}
            readOnly={readOnly}
          />
          {name === feedback?.inputFieldName && <Form.Control.Feedback type='invalid'>{feedback?.errorMessage}</Form.Control.Feedback>}
        </Col>

      </Form.Group>
    </>
  );
};

export default Input;
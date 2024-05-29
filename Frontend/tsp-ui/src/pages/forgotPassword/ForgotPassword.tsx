import React, { useState } from 'react';
import './forgotPassword.css';
import Form from '../../components/form/Form';
import FormInput from '../../components/input/Input';
import Button from '../../components/button/Button';
import { useNavigate } from 'react-router-dom'
import ForgotPasswordConfirmation from './ForgotPasswordConfirmation';
import userProfileApi from '../../apis/userProfileManagement';
import { EmptyInputValidate } from '../../utility/EmptyInputValidate';
import { Validation } from '../../utility/models/validation/Validation';
import ErrorMessage from '../../components/error/ErrorMessage';

const ForgotPassword = () => {
    const [email, setEmail] = useState('');
    const [showConfirmation, setShowConfirmation] = useState(false);
    const [feedback, setFeedback] = useState<Validation>({ isValid: false, errorMessage: "", inputFieldName: "" });
    const [apiErrors, setApiError] = useState<string>("");

    const navigate = useNavigate();

    const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
      setEmail(e.target.value);
    };

    const forgotPassword = async (emailId:string) => 
    {
          const promise = await userProfileApi.forgotPassword(emailId);
          const response = await promise.data.payLoad;
          return response;
    };

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => 
    {
      e.preventDefault();
      const mailIdMessage = EmptyInputValidate(email);
      if (mailIdMessage && mailIdMessage !== null) {
        setFeedback((feedback) => ({ isValid: true, errorMessage: mailIdMessage, inputFieldName: "mailId" }))
      }
      else
      {
        forgotPassword(email).then(result =>
          {
            setShowConfirmation(true);
          }).catch(error=>{
            const message = error.response?.data?.payLoad;
            if (typeof message === 'string') {
              setApiError(message);
            }
          }
          );
        }
      };

  return (
    <div className="forget-password-container">
        <form onSubmit={handleSubmit}>
        <label htmlFor="email">Email:</label>
        <input
          type="email"
          id="email"
          placeholder="Enter your login ID"
          value={email}
          onChange={handleEmailChange}
          required
        />
        <button className="btn btn-primary btn-sm" type="submit">
          Continue
        </button>
        {apiErrors && <ErrorMessage message={apiErrors} />}
        <ForgotPasswordConfirmation
          showModal={showConfirmation}
          onHide={() => setShowConfirmation(false)}
          emailId={email}
        />
      </form>
    </div>
  );
};

export default ForgotPassword;




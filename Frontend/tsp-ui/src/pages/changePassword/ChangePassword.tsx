import React, { useState } from 'react';
import Form from '../../components/form/Form';
import Button from '../../components/button/Button';
import FormInput from '../../components/input/Input';
import './changePassword.css';
import { useNavigate } from 'react-router-dom';
import { ChangePassword } from '../../utility/models/changePassword/ChangePassword'
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../store/rootReducer';
import userApi from '../../apis/userManagement';
import { ResetResponseObject } from '../../utility/models/changePassword/ChangePasswordResponseObject';
import { clearUser } from '../../store/userReducer/userActions';
import { AxiosResponse } from 'axios';


function Reset() {
    const user = useSelector((state: RootState) => state.user.user);
    const initialFormData = {
        emailId: user?.emailId,
        newPassword: '',
        confirmPassword: '',
    };
    const navigate = useNavigate();
    const dispatch = useDispatch()
    const [apiErrors, setApiError] = useState<string>("")
    const [apiSuccessMessage, setApiSuccessMessage] = useState<string>("")
    const [newPasswordDetails, setNewPasswordDetails] = useState<ChangePassword>(initialFormData);
    const [error, setError] = useState<string | null>(null);

    const validatePasswords = (newPassword: string, confirmPassword: string): boolean => {
        const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d.*\d)(?=.*[!@#$%^&*(),.?":{}|<>]).{8,15}$/;
        if (!passwordRegex.test(newPassword)) {
            return false;
        }
        return true;
    };

    const handleResetSubmit = async (formData: ChangePassword) => {

        if (validatePasswords(formData.newPassword, formData.confirmPassword)) {

            if (formData.newPassword === formData.confirmPassword) {

                if (user?.emailId) {
                    setNewPasswordDetails({
                        ...formData,
                        emailId: user.emailId,
                    });
                }
                try {
                    const promise: AxiosResponse = await userApi.changePassword(newPasswordDetails);
                    const response: ResetResponseObject | any = await promise.data;
                    setApiSuccessMessage(response.payload)
                    dispatch(clearUser());
                    navigate('/login');
                } catch (error: any) {
                }

            } else {
                setError('Passwords not matching');
            }
        } else {
            setError('Password does not meet the specified criteria.');
        }

    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setNewPasswordDetails({
            ...newPasswordDetails,
            [e.target.name]: e.target.value,
        });
    };


    return (
        <div className='reset-page'>
            <Form onSubmit={handleResetSubmit} formData={newPasswordDetails} setFormData={setNewPasswordDetails}>
                <div className='custom-form'>
                    <h1>Password Reset</h1>
                    <FormInput label='New Password' name='newPassword' type='password' value={newPasswordDetails.newPassword} onChange={(e) => { setNewPasswordDetails({ ...newPasswordDetails, [e.target.name]: e.target.value }) }} />
                    <FormInput label='Confirm Password' name='confirmPassword' type='password' value={newPasswordDetails.confirmPassword} onChange={(e) => { setNewPasswordDetails({ ...newPasswordDetails, [e.target.name]: e.target.value }) }} />
                    <Button type='submit'>Submit</Button>
                    {error && <p className='error-message'>{error}</p>}
                </div>
            </Form>
        </div>
    );
}
export default Reset;
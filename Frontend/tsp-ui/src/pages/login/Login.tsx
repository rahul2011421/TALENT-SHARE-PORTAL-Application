import { useState } from 'react';
import './login.css';
import Form from '../../components/form/Form';
import FormInput from '../../components/input/Input';
import Button from '../../components/button/Button';
import userApi from '../../apis/userManagement';
import { useDispatch } from 'react-redux';
import { setUser } from '../../store/userReducer/userActions';
import { Link, useNavigate } from 'react-router-dom'
import { LoginUser } from '../../utility/models/login/LoginUser';
import { Validation } from '../../utility/models/validation/Validation';
import { EmptyInputValidate } from '../../utility/EmptyInputValidate';
import ErrorMessage from '../../components/error/ErrorMessage';
import Col from 'react-bootstrap/Col';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import { Image } from 'react-bootstrap';

function Login() {
  const initialFormData = { mailId: '', password: '' }
  const [loginCredentials, setFormData] = useState<LoginUser>(initialFormData);
  const [feedback, setFeedback] = useState<Validation>({ isValid: false, errorMessage: "", inputFieldName: "" });
  const [apiErrors, setApiError] = useState<string>("");
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleLogin = async (loginCredentials: LoginUser) => {

    const mailIdMessage = EmptyInputValidate(loginCredentials.mailId);
    const passwordMessage = EmptyInputValidate(loginCredentials.password)
    if (mailIdMessage && mailIdMessage !== null) {
      setFeedback((feedback) => ({ isValid: true, errorMessage: mailIdMessage, inputFieldName: "mailId" }))
    }
    else if (passwordMessage && passwordMessage !== null) {
      setFeedback(() => ({ isValid: true, errorMessage: passwordMessage, inputFieldName: "password" }))
    }
    else {
      try {
        const promise = await userApi.loginUser(loginCredentials);
        const response = await promise.data;

        dispatch(setUser(response.payLoad));

        if (response.payLoad.passwordReset) {
          navigate('/reset');
        } else {
          switch (response.payLoad.userGroup.ugname) {
            case "Admin": navigate('/admin');
              break;

            case "Mentor": navigate('/dashboard');
              break;

            case "Mentee": navigate('/dashboard');
              break;

            case "Manager": navigate('/dashboard');
              break;
            default: navigate('/dashboard');
          }
        }
      } catch (error: any) {
        const message = error.response?.data?.payLoad;
        if (typeof message === 'string') {
          setApiError(message);
        } else {
          setApiError(message?.password);
        }
      }
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...loginCredentials, [e.target.name]: e.target.value })
    setApiError("");
  }

  return (
    <>
      <Container fluid>
        <Row className='p-0' lg={12} style={{ height: '100vh' }}>
          <Col xs={6} className='p-0' ><Image rounded style={{ height: '100vh', width: '100vw' }} src="bluescreen.png" fluid /></Col>
          <Col xs={6} className='p-5 d-flex flex-column justify-content-center' >
            <div className='p-5'>
              {apiErrors && <ErrorMessage message={apiErrors} />}
              <div className='d-flex flex-column align-items-start p-3 pb-0'>
                <span className='login-hello_text'>Hello Again!</span>
                <span className='login-wellcome_tag_text'>Welcome back</span>
              </div>
              <div className='p-0'>
                <Form onSubmit={handleLogin} formData={loginCredentials} setFormData={setFormData}>
                  <FormInput feedback={feedback} setFeedback={setFeedback} label="User Email" name="mailId" type="email" value={loginCredentials.mailId} onChange={handleChange} />
                  <FormInput feedback={feedback} setFeedback={setFeedback} label="Password" name="password" type="password" value={loginCredentials.password} onChange={handleChange} />
                  <div className='d-flex justify-content-around mt-5'>
                    <Button type="submit">Login</Button>
                    <Link to="/forgot-password"><Button type="button">Forgot password</Button></Link>
                  </div>
                </Form>
              </div>
            </div>
          </Col>
        </Row>
      </Container>
    </>
  );
}

export default Login;
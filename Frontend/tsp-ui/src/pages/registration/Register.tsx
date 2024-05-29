import React, { useEffect, useState } from 'react';
import "./register.css"
import ReusableForm from '../../components/form/Form';
import FormInput from '../../components/input/Input';
import Button from '../../components/button/Button';
import userApi from '../../apis/userManagement';
import DropdownSelection from '../../components/dropdown/dropdownSelection';
import { RegisterUser } from '../../utility/models/register/RegisterUser';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom'
import { RegisterUserConstructedForBackend } from '../../utility/models/register/RegisterUser';
import { RootState } from '../../store/rootReducer';
import { Validation } from '../../utility/models/validation/Validation';
import { EmptyInputValidate } from '../../utility/EmptyInputValidate';
import { AxiosResponse } from 'axios';
import ErrorMessage from '../../components/error/ErrorMessage';
import { staticDataFetch } from '../../apis/staticDataFetch';
import { StaticData } from '../../utility/models/staticData/staticData';



function Register() {
  const initialFormData = { name: "", emailId: "", userGroup: "", departmentUnit: "", createdBy: "" }
  const [registerFormData, setRegisterFormData] = useState<RegisterUser>(initialFormData);
  const [feedback, setFeedback] = useState<Validation>({ isValid: false, errorMessage: "", inputFieldName: "" });
  const user = useSelector((state: RootState) => state.user.user);
  const [dropDownData, setDropDownData] = useState<StaticData>({ userGroups: [], bussinessUnits: [] })
  const navigate = useNavigate()
  const [apiErrors, setApiError] = useState<string>("");

  useEffect(() => {
    dropdownSelectionItems();
  }, [])

  const transformedArray = (data: Array<any>, dropDownDataName: string) => {
    if (dropDownDataName == "bussinessUnits") {
      console.log("dropDownDataName", dropDownDataName, "data", data);
      return data.map(item => ({
        label: item.buName,
        value: item.buName
      }))
    } else if (dropDownDataName == "userGroups")
      console.log("dropDownDataName", dropDownDataName, "data", data);
    return data.map(item => ({
      label: item.ugname,
      value: item.ugname
    }))

  }

  const setData = async (promise: any, dropDownDataName: string) => {
    const response = await promise;
    const res = await response.data.payLoad;

    setDropDownData((prevDropDownData) => ({ ...prevDropDownData, [dropDownDataName]: transformedArray(res, dropDownDataName) }))

  }

  const dropdownSelectionItems = async () => {

    try {
      await setData(await staticDataFetch.getBUGroups(), 'bussinessUnits');
      await setData(await staticDataFetch.getUserGroups(), 'userGroups');
    } catch (error: any) {
      setApiError(error?.message)
    }
  }

  const constructedObject: RegisterUserConstructedForBackend = {
    name: registerFormData.name,
    emailId: registerFormData.emailId,
    userGroup: { ugname: registerFormData.userGroup, active: true },
    departmentUnit: { buName: registerFormData.departmentUnit },
    createdBy: user?.emailId
  };

  const handleSubmit = async (registerFormData: RegisterUser) => {

    const mailIdMessage = EmptyInputValidate(registerFormData.emailId);
    const name = EmptyInputValidate(registerFormData.name);
    const departmentUnit = EmptyInputValidate(registerFormData.departmentUnit);
    const userGroup = EmptyInputValidate(registerFormData.userGroup)
    if (name && name !== null) {
      setFeedback(() => ({ isValid: true, errorMessage: name, inputFieldName: "name" }))
    }
    else if (mailIdMessage && mailIdMessage !== null) {
      setFeedback(() => ({ isValid: true, errorMessage: mailIdMessage, inputFieldName: "emailId" }))
    }
    else if (userGroup && userGroup !== null) {
      setFeedback(() => ({ isValid: true, errorMessage: userGroup, inputFieldName: "userGroup" }))
    }
    else if (departmentUnit && departmentUnit !== null) {
      setFeedback(() => ({ isValid: true, errorMessage: departmentUnit, inputFieldName: "departmentUnit" }))
    }
    else {
      try {
        const promise: AxiosResponse = await userApi.registerUser(constructedObject)
        const res: string = await promise.data;
        navigate(0);
      } catch (error: any) {
        const message = error.response?.data?.payLoad;
        if (typeof message === 'string') {
          setApiError(message);
        } else {
          setApiError(message?.createdBy);
        }
      }
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement> | React.ChangeEvent<HTMLSelectElement>) => {
    setApiError("")
    setFeedback({ ...feedback, isValid: false, inputFieldName: e.target.name, errorMessage: "" })
    setRegisterFormData({ ...registerFormData, [e.target.name]: e.target.value })
    const { name, value } = e.target
    const message = EmptyInputValidate(e.target.value);
    if (message && message !== null) {
      setFeedback((feedback) => ({ isValid: true, errorMessage: message, inputFieldName: name }))
    }
  }

  return (
    <>
      {apiErrors && <ErrorMessage message={apiErrors} />}
      <ReusableForm onSubmit={handleSubmit} formData={registerFormData} setFormData={setRegisterFormData}>
        <div className='m-auto'>
          <FormInput feedback={feedback} setFeedback={setFeedback} label="User Name" name="name" type="text" value={registerFormData.name} onChange={handleChange} />
          <FormInput feedback={feedback} setFeedback={setFeedback} label="User Email" name="emailId" type="email" value={registerFormData.emailId} onChange={handleChange} />
          <DropdownSelection feedback={feedback} setFeedback={setFeedback} label="Select User Group" name="userGroup" value={registerFormData.userGroup}
            options={dropDownData?.userGroups}
            onChange={handleChange} />
          <DropdownSelection feedback={feedback} setFeedback={setFeedback} isRequired={true} label="Select BU Group" name="departmentUnit" value={registerFormData.departmentUnit}
            options={dropDownData?.bussinessUnits}
            onChange={handleChange} />
          <div className='d-flex mb-2 flex-wrap justify-content-between'>
            <Button type="submit" >Create User</Button>
            <Button type="reset" onClick={() => { setRegisterFormData(initialFormData) }}>Clear</Button>
          </div>
        </div>
      </ReusableForm>
    </>
  );
}

export default Register;


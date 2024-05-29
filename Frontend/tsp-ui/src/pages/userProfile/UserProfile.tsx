import { Button } from "react-bootstrap";
import ReusableForm from "../../components/form/Form";
import FormInput from '../../components/input/Input';
import {  useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import userProfileApi from "../../apis/userProfileManagement";
import { setUserProfile } from "../../store/userProfileReducer/userProfileAction";
import { RootState } from "../../store/rootReducer";
import { UserProfilemodel } from "../../utility/models/userProfile/UserProfilemodel";

function UserProfile() {

  const user = useSelector((state: RootState) => state.user.user!);
  
  
  const [updateFormData, setNewUpdateFormData] = useState<UserProfilemodel>({
    name: user?.name,
    emailId: user?.emailId, departmentUnit: '', location: '',
    professionalSummary: '', skillCategory: '', technicalSkill: [], photo: null
  });
  const dispatch = useDispatch() 

  const handleSubmit = (updateUserProfile: any) => {

    userProfileApi.userProfileUpdate(updateUserProfile)
      .then(userProfile => {
        dispatch(setUserProfile(userProfile.data));
      }).catch(error => {  })
  }
  
  return (
    <div>
      <div className='m-auto mt-5 container w-50'>
        <ReusableForm onSubmit={() => handleSubmit(updateFormData)} formData={updateFormData} setFormData={setNewUpdateFormData}>
          <FormInput label="Name" name="name" type="text" value={updateFormData.name}onChange={e => {setNewUpdateFormData({ ...updateFormData, [e.target.name]: e.target.value }) }} readOnly/>
          <FormInput label="User Email-ID" name="emailId" type="email" value={updateFormData.emailId}onChange={e => { setNewUpdateFormData({ ...updateFormData, [e.target.name]: e.target.value }) }} readOnly/>
          <FormInput label="Department Unit" name="departmentUnit" type="text" value={updateFormData.departmentUnit} onChange={e => { setNewUpdateFormData({ ...updateFormData, [e.target.name]: e.target.value }) }} />
          <FormInput label="Location" name="location" type="text" value={updateFormData.location} onChange={e => { setNewUpdateFormData({ ...updateFormData, [e.target.name]: e.target.value }) }} />
          <FormInput label="Professional Summary" name="professionalSummary" type="text" value={updateFormData.professionalSummary} onChange={e => { setNewUpdateFormData({ ...updateFormData, [e.target.name]: e.target.value }) }} />
          <FormInput label="Upload Photo" name="uploadPhoto" type="file" value={""} onChange={e => {setNewUpdateFormData({ ...updateFormData, [e.target.name]: e.target.value }) }} />
          <Button type="submit">Submit</Button>
        </ReusableForm>
      </div>
    </div>
  );
}

export default UserProfile;
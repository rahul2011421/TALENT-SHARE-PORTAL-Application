import { BASE_URL } from './baseUrl';
import { RegisterUser } from '../utility/models/register/RegisterUser';
import { LoginUser } from '../utility/models/login/LoginUser';
import axios, { AxiosResponse } from 'axios';
import { ChangePassword } from '../utility/models/changePassword/ChangePassword';
import { RegisterUserConstructedForBackend } from '../utility/models/register/RegisterUser';
import { GlobalResponse } from '../utility/models/GlobalResponse';

const userApi = {
  url: BASE_URL + "/admin/users",

  registerUser: (registerUser: RegisterUserConstructedForBackend) => {
    return axios.post(userApi.url+"/register", registerUser)
  },

  loginUser: (loginUser: LoginUser) => {
    const encryptedPassword = btoa(loginUser.password);
    const user = { ...loginUser, password: encryptedPassword }
    ;
    
    return axios.post(userApi.url + "/login", user)
  },

  changePassword: (changePassword: ChangePassword) => {
    const encodedPassword = btoa(changePassword.newPassword);
    const encodedConformPassword = btoa(changePassword.confirmPassword);
    const newPasswordDetails = { ...changePassword, newPassword: encodedPassword,  confirmPassword:encodedConformPassword}
    return axios.post(userApi.url + "/reset", newPasswordDetails)
  },

  deleteUser: (emailId: String) => {
    return axios.delete(userApi.url + "/deleteUserDetails/"+ emailId )
  }
}

export default userApi
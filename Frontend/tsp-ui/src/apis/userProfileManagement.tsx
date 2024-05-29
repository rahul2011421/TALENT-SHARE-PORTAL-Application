import axios from "axios";
import { UserProfilemodel } from "../utility/models/userProfile/UserProfilemodel";
import { BASE_URL } from "./baseUrl";


const userProfileApi = {
  url: BASE_URL + "/users",
  adminUrl: BASE_URL + "/admin/users",

  userProfileUpdate: (userProfile: UserProfilemodel) => {
    return axios.post(userProfileApi.url + "/userProfileUpdate/", userProfile)
  },

  forgotPassword: (emailId: string) => {
    return axios.get(userProfileApi.adminUrl + "/forgetPassword/" + emailId);
  }
}
export default userProfileApi;
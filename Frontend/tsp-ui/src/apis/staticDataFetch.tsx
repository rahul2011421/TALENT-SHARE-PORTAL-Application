import axios from "axios"
import { BASE_URL } from "./baseUrl";



export const staticDataFetch={
    url:BASE_URL,
    getUserGroups:()=>{
        return axios.get(staticDataFetch.url+'/getAllUsersGroup');
    },
    getBUGroups:()=>{
       return axios.get(staticDataFetch.url+'/admin/users/getAllDepartments');
    }
}
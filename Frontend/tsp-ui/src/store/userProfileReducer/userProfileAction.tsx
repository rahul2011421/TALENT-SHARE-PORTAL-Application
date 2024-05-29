
import { UserProfilemodel } from "../../utility/models/userProfile/UserProfilemodel";

export const setUserProfile= (userProfile:UserProfilemodel)=>({
    type:"SET_USERPROFILE",
    payload: userProfile,
});

export const clearUserProfile =() =>({
    type:"CLEAR_USERPROFILE",
});

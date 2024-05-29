import { UserProfilemodel } from "../../utility/models/userProfile/UserProfilemodel";

interface UserState {
  user: UserProfilemodel | null;
}

const initialState: UserState = {
  user: null,
};

const userProfileReducer = (state = initialState, action: any): UserState => {
  switch (action.type) {
    case 'SET_USER':
      return {
        ...state,
        user: action.payload,
      };
    case 'CLEAR_USER':
      return {
        ...state,
        user: null,
      };
    default:
      return state;
  }
};

export default userProfileReducer;

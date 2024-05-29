import {LoginResponseObject} from '../../utility/models/login/LoginResponseObject'

export const setUser = (user: LoginResponseObject) => ({
  type: 'SET_USER',
  payload: user,
});

export const clearUser = () => ({
  type: 'CLEAR_USER',
});

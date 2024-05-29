import { LoginResponseObject } from "./login/LoginResponseObject"

export interface GlobalResponse{
    status:string,
    code:number,
    message:string,
    payLoad: LoginResponseObject 
}
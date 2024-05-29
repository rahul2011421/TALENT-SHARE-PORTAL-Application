
const namesValidation=(data:string , callback:any)=>{
    if(!data){
        return callback({isValid:true, message:"Enter Valid input"});
    }
    return "Looks good"
}

export default namesValidation;
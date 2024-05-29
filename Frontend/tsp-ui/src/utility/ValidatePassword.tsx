
export const ValidatePassword=(password:string):string|null=>{
    const minLength = 8;
    const lowerCaseRegex=/[a-z]/;
    const upperCaseRegex=/[A-Z]/;
    const digitRegex=/\d/;
    const specialCharRegex=/[!@#$%^&*(),.?":{}|<>]/;

    if(password.length<=minLength){
        return 'Password should be at least 8 characters long';
    };
    if(!lowerCaseRegex.test(password)){
        return 'Password should contain at least one lowercase letter';
    };
    if(!upperCaseRegex.test(password)){
        return 'Password should contain at least one uppercase letter';
    };
    if(!digitRegex.test(password)){
        return 'Password should contain at least one digit';
    };
    if(!specialCharRegex.test(password)){
        return 'Password should contain at least one special character';
    };

    return null;
};
    export interface RegisterUser {
        name: string,
        emailId: string ,
        userGroup: string,
        departmentUnit: string,
    }


    export interface RegisterUserConstructedForBackend {
        name: string,
        emailId: string | undefined,
        userGroup: userGroup,
        departmentUnit: departmentUnit,
        createdBy: string | undefined
    }

    export interface departmentUnit {
        buName: string
    }

    export interface userGroup {
        ugname: string
        active:boolean
    }

export interface UserProfilemodel{
    name:string,
    emailId:string,
    departmentUnit:string,
    location:string,
    professionalSummary:string,
    skillCategory:string,
    technicalSkill:string[],
    photo:File | null
}
export interface LoginResponseObject {
  name: string;
  emailId: string;
  passwordReset:boolean
  userGroup: UserGroup ;
  departmetUnit: DepartmentUnit;
  userProfile: userProfile | null;
}

export interface UserGroup {
  ugname: string;
  active:boolean
}

export interface DepartmentUnit {
  buName: string;
}

export interface userProfile {
  Location: string,
  professionalSummary: string,
  location:string,
  SkillCategory: SkillCategory;
  technicalSkill: string[],
  photo: File | null
}

export interface SkillCategory {
  categoryName: string
}



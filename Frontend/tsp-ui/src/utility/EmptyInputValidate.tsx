export const EmptyInputValidate = (fielData: string): string | null=> {
    if (fielData === "") {
        return "Field Can not be empty";
    }
    return null;
}
import React, { ReactNode, FormEvent, ChangeEvent } from 'react';
import Form from 'react-bootstrap/Form';
import Card from 'react-bootstrap/Card';


interface FormChildProps {
  onChange: (event: ChangeEvent<HTMLInputElement>) => void;
  value: any;
}

interface ReusableFormProps {
  children?: React.ReactNode;
  onSubmit: (formData: any) => void;
  formData: any;
  setFormData: React.Dispatch<React.SetStateAction<any>>;
  feedBack?: any;
}

function ReusableForm({ children, onSubmit, formData, setFormData }: ReusableFormProps) {

  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [event.target.name]: event.target.value,
    });
  };

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    onSubmit(formData);
  };

  const cloneChild = (child: ReactNode): ReactNode => {
    if (React.isValidElement(child)) {
      const childProps: FormChildProps = {
        onChange: handleChange,
        value: formData[child.props.name] || '',
      };
      return React.cloneElement(child, childProps);
    }
    return child;
  };

  return (
    <>
      <div className='p-3 m-auto rounded-3 '>
        <Form onSubmit={(e: FormEvent<HTMLFormElement>) => handleSubmit(e)}>
          {React.Children.map(children, cloneChild)}
        </Form>
      </div>
    </>
  );
}

export default ReusableForm;

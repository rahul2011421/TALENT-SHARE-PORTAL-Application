import React from 'react';
import BButton from 'react-bootstrap/Button';
import './button.css'

interface ButtonProps {
  children?: React.ReactNode;
  type: 'button' | 'submit' | 'reset' | undefined;
  onClick?: ()=>void
  bootstrap_class_styles?:string
}

function Button({ children, type, onClick, bootstrap_class_styles }: ButtonProps) {
  return (
    <BButton type={type} onClick={onClick} className='btn-primary '>
      {children}
    </BButton>
  );
}

export default Button;



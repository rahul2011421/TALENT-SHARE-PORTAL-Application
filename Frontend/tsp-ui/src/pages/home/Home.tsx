import { useState } from 'react';
import Button from '../../components/button/Button';
import { Link, useNavigate } from 'react-router-dom'

function Home() {

  return (
    <div>
      <div className='d-flex justify-content-evenly mt-5 m-auto w-50'>
        <Link to="/login">
          <Button type='button'>SignIn</Button>
        </Link>
      </div>
    </div>
  );
}

export default Home;

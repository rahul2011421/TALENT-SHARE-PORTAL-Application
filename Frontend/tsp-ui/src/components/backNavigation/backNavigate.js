import React from 'react'
import { Reply } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';

function BackNavigate() {
    const navigate = useNavigate()  
    return (
        <div className='d-flex ' onClick={() => { navigate(-1) }}>
            <Reply />
            <span className="text-decoration-underline"  >Back</span>
        </div>
    )
}

export default BackNavigate
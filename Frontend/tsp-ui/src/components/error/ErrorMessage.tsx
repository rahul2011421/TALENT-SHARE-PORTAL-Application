import React from 'react'

interface ErrorProps {
    message: string;
}

function ErrorMessage(message: ErrorProps) {
    return (
        <>
            <div className='text-danger'>
                {message.message}
            </div>
        </>
    )
}

export default ErrorMessage
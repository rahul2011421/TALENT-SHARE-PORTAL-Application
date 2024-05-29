import React from 'react';
import { Modal, Button } from 'react-bootstrap';

interface ForgotPasswordConfirmationProps {
  showModal: boolean;
  onHide: () => void;
  emailId: string;
}

function ForgotPasswordConfirmation({
  showModal,
  onHide,
  emailId,
}: ForgotPasswordConfirmationProps) {
  return (
    <>
      <Modal show={showModal} onHide={onHide}>
        <Modal.Header closeButton>
          <Modal.Title>Forget Password Confirmation</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="alert alert-info">
            We have reset your password for the first login and its shared to your mail ID, please change your own password once you login to the portal.
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="primary" onClick={onHide}>
            OK
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}

export default ForgotPasswordConfirmation;

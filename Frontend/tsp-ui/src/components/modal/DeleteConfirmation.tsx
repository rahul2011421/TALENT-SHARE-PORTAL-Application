import React, { useState } from 'react';
import { Modal, Button } from 'react-bootstrap';
import userApi from '../../apis/userManagement';


interface DeleteProps {
  showModal: boolean;
  onHide: () => void;
  emailId: string;
}

function handleDelete(emailId: string, onHide: any, setShowSuccessModal: any) {
 
  userApi
    .deleteUser(emailId)
    .then((res) => {
      setShowSuccessModal(true); 
    })
    .catch((error) => {
    })
    .finally(() => {
      onHide(); 
    });
}

function DeleteConfirmation({ showModal, onHide, emailId }: DeleteProps) {
  const [showSuccessModal, setShowSuccessModal] = useState(false);

  return (
    <>
      <Modal show={showModal} onHide={onHide}>
        <Modal.Header closeButton>
          <Modal.Title>Delete Confirmation</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="alert alert-danger">
            Are you sure you want to delete the user? If you delete the user, it will not be possible to recover again to the portal.
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button
            variant="danger"
            onClick={() => handleDelete(emailId, onHide, setShowSuccessModal)}
          >
            Yes
          </Button>
          <Button variant="default" onClick={onHide}>
            No
          </Button>
        </Modal.Footer>
      </Modal>

      <Modal show={showSuccessModal} onHide={() => setShowSuccessModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>User Deleted Successfully</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          The user has been deleted successfully!
        </Modal.Body>
        <Modal.Footer>
          <Button variant="default" onClick={() => setShowSuccessModal(false)}>
            OK
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}

export default DeleteConfirmation;
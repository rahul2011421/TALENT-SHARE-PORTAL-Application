import React, { ReactNode } from 'react'
import BModal, { ModalProps } from "react-bootstrap/Modal"


interface ModelProps {
  show: boolean,
  onHide: () => void,
  modalHeader?: ReactNode,
  modalTitle?: ReactNode,
  modalBody?: ReactNode,
  modalFooter?: ReactNode
}


function Modal({ show, onHide, modalHeader, modalTitle, modalBody, modalFooter }: ModalProps) {
  return (
    <>
      <BModal show={show} onHide={onHide} backdrop="static" keyboard={false}>
        <BModal.Header closeButton>
          {modalTitle && <BModal.Title>{modalTitle}</BModal.Title>}
        </BModal.Header>
        {modalBody && <BModal.Body>{modalBody}</BModal.Body>}
        {modalFooter && <BModal.Footer>{modalFooter}</BModal.Footer>}
      </BModal>
    </>
  )
}

export default Modal
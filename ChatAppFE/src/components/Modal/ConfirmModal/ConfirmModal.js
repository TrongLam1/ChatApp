import React from 'react'
import { Modal, Button } from "react-bootstrap";

const ConfirmModal = (props) => {

    const { isOpen, closeModal, message, onConfirm } = props;

    return (
        <Modal show={isOpen} onHide={closeModal}>
            <Modal.Header closeButton>
                <Modal.Title>Delete Confirmation</Modal.Title>
            </Modal.Header>
            <Modal.Body><div className="alert alert-danger">{message}</div></Modal.Body>
            <Modal.Footer>
                <Button variant="default" onClick={closeModal}>
                    Cancel
                </Button>
                <Button variant="danger" onClick={onConfirm}>
                    Delete
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default ConfirmModal;
'use client'

import { Button, Modal } from "react-bootstrap";

export default function ConfirmModal(props: any) {

    const { isOpen, closeModal, message, onConfirm } = props;

    return (
        <Modal show={isOpen} onHide={closeModal}>
            <Modal.Header closeButton>
                <Modal.Title>Xác nhận</Modal.Title>
            </Modal.Header>
            <Modal.Body><div className="alert alert-danger">{message}</div></Modal.Body>
            <Modal.Footer>
                <Button variant="default" onClick={closeModal}>
                    Hủy
                </Button>
                <Button variant="danger" onClick={onConfirm}>
                    Xóa
                </Button>
            </Modal.Footer>
        </Modal>
    );
}
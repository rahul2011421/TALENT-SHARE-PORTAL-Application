import axios from "axios";
import { useEffect, useState } from "react";
import { PaginationResponse } from "../../utility/models/pagination/PaginationResponse";
import Modal from "../../components/modal/Modal";
import Register from '../registration/Register';
import DeleteConfirmation from "../../components/modal/DeleteConfirmation";  //added
import { BASE_URL } from "../../apis/baseUrl";

function AdminUserManagement() {
    const [numberOfPages, setTotalRecords] = useState<number>(0);
    const [users, setUsers] = useState<PaginationResponse[]>([{ name: '', emailId: '', userGroup: '' }]);
    const [show, setShow] = useState<boolean>(false);
    const [showDelete, setShowDelete] = useState<boolean>(false);   //Added
    const [deleteItem, setDeleteItem] = useState<string>("");


    const handleClose = () => setShow(false);
    const handleShow = () => { setShow(true) };



    useEffect(() => {

        const fetchdata = async () => {

            try {

                const promise = await axios.post(BASE_URL+"/admin/users/pagination/1");
                const response = await promise.data;
                setUsers(response.payLoad.users)
                const total = (response.payLoad.totalRecords / 5) + 1;
                setTotalRecords(total)

            } catch (error: any) {
            }

        }
        fetchdata();
    }, [showDelete]);

    const pages = Array.from({ length: numberOfPages }, (_, index) => index);

    return (
        <div className="container d-flex flex-column">
            <div>
                <button type="button" style={{ pointerEvents: 'none' }} className="btn btn-primary btn-lg float-start rounded-0 mt-5">USER MANAGEMENT</button>
                <button onClick={handleShow} type="button" className="btn btn-primary btn-lg float-end rounded-0 mt-5">New User</button>
                <Modal
                    show={show}
                    onHide={handleClose}
                    backdrop="static"
                    keyboard={true}
                    modalTitle="Creating User"
                    modalBody={<Register />}
                />


            </div>
            <div className="p-2 justify-content-between">
                <form className="d-flex">
                    <button className="btn btn-primary rounded-0" style={{ pointerEvents: 'none' }}>Email</button>
                    <input className="form-control me-2" type="search" placeholder="Search" aria-label="Search" />
                    <button className="btn btn-primary rounded-0" type="submit">Search</button>
                </form>
            </div>
            <div>
                <table className="table">
                    <thead>
                        <tr>
                            <th scope="col">Name</th>
                            <th scope="col">Email Id</th>
                            <th scope="col">UserType</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.map((user) => (
                            <tr key={user.emailId}>
                                <td className="text">{user.name}</td>
                                <td>{user.emailId}</td>
                                <td>{user.userGroup}</td>
                                <td className="d-flex justify-content-around">
                                    <button className="btn btn-primary btn-sm" onClick={() => { setDeleteItem(user.emailId); setShowDelete(true) }}>DELETE</button></td>
                                <DeleteConfirmation
                                    showModal={showDelete}
                                    onHide={handleClose}
                                    emailId={deleteItem}
                                ></DeleteConfirmation>

                            </tr>

                        ))}
                    </tbody>
                </table>

            </div>
            <div className="fixed-bottom">
                <nav aria-label="Page navigation example">
                    <ul className="pagination justify-content-center">
                        {

                            pages.map((ind) => (
                                <button className="btn p-0" key={ind + 1} onClick={() => {
                                    axios.post(BASE_URL+"/admin/users/pagination/" + (ind + 1))
                                        .then((response) => {
                                            setUsers(response.data.payLoad.users)
                                            const total = (response.data.PayLoad.totalRecords / 5) + 1;

                                            setTotalRecords(total)
                                        }).catch((err) => {

                                        })
                                }}>
                                    <li className="page-item"><a className="page-link">{ind + 1}</a></li></button>
                            ))
                        }
                    </ul>
                </nav>
            </div>
        </div>
    );
}

export default AdminUserManagement
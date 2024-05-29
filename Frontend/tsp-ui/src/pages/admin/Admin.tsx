import { Link } from "react-router-dom";
import Button from "../../components/button/Button";

function Admin() {

    return (
        <>
            <div className="mt-5 d-flex justify-content-center">
                <div className="m-3"><Link to="/user-manage"> <Button type="button" >User Management</Button></Link></div>
                <div className="m-3"><Button type="button">Feedback</Button></div>
            </div>
            <div className="d-flex justify-content-center">
                <div className="m-3"><Button type="button">Talent Share Report</Button></div>
                <div className="m-3"><Button type="button">Mentorship Session Record</Button></div>
                <div className="m-3"><Button type="button">BU Session</Button></div>
                <div className="m-3"><Link to="/user-Profile"><Button type="button">User Profile</Button></Link></div>
                <div className="m-3"><Link to="/archive"><Button type="button">Archive</Button></Link></div>
            </div>


        </>
    )
}

export default Admin
import { useSelector } from 'react-redux';
import { RootState } from '../../store/rootReducer';


function Dashboard() {
    const user = useSelector((state: RootState) => state.user.user);

    return (
        <>
            {
                <div>Welcome {user?.emailId}
                name {user?.name}
                Group {user?.userGroup.ugname}</div>
              
            }
        </>
    )
}

export default Dashboard
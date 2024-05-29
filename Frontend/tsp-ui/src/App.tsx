import './App.css';
import { BrowserRouter as Router } from 'react-router-dom'
import { Provider } from 'react-redux';
import store from './store/store';
import 'bootstrap/dist/css/bootstrap.min.css';
import CustomeRoutes from './routing/CustomeRoutes';



function App() {
  return (
    <Provider store={store}>
      <div className="App">
        <Router>
            <CustomeRoutes />
        </Router>
      </div>
    </Provider>
  );
}

export default App;

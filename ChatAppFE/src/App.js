import './App.scss';
import { ToastContainer, toast } from 'react-toastify';
import AppRoutes from './routes/AppRoutes';

function App() {
  return (
    <>
      <div className="app-container">
        <AppRoutes />
      </div>

      <ToastContainer
        position="top-right"
        autoClose={1500}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
      />
    </>
  );
}

export default App;

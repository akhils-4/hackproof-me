import { useNavigate } from "react-router-dom";
import ScanForm from "../components/ScanForm";
import { Shield, Search, Lock } from "lucide-react";

const Home = () => {
  const navigate = useNavigate();

  const handleScanComplete = (scanId) => {
    navigate(`/results/${scanId}`);
  };

  return (
    <div className="max-w-4xl mx-auto">
      <div className="text-center mb-12">
        <h1 className="text-4xl md:text-5xl font-bold text-gray-900 mb-4">
          Check Your Cybersecurity Risk
        </h1>
        <p className="text-lg md:text-xl text-gray-600">
          Analyze your digital footprint and get personalized security
          recommendations
        </p>
      </div>

      <div className="bg-white rounded-lg shadow-lg p-8 mb-8">
        <ScanForm onScanComplete={handleScanComplete} />
      </div>

      <div className="grid md:grid-cols-3 gap-6">
        <div className="bg-white p-6 rounded-lg shadow hover:shadow-md transition-shadow">
          <div className="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center mb-4">
            <Search className="w-6 h-6 text-blue-600" />
          </div>
          <h3 className="font-semibold text-lg mb-2">Breach Detection</h3>
          <p className="text-gray-600 text-sm">
            Check if your email has been compromised in data breaches
          </p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow hover:shadow-md transition-shadow">
          <div className="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center mb-4">
            <Lock className="w-6 h-6 text-green-600" />
          </div>
          <h3 className="font-semibold text-lg mb-2">Password Analysis</h3>
          <p className="text-gray-600 text-sm">
            Evaluate your password strength with advanced algorithms
          </p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow hover:shadow-md transition-shadow">
          <div className="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center mb-4">
            <Shield className="w-6 h-6 text-purple-600" />
          </div>
          <h3 className="font-semibold text-lg mb-2">Risk Scoring</h3>
          <p className="text-gray-600 text-sm">
            Get a comprehensive security score with actionable insights
          </p>
        </div>
      </div>
    </div>
  );
};

export default Home;

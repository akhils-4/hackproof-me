import { useNavigate } from "react-router-dom";
import HistoryTable from "../components/HistoryTable";
import { History as HistoryIcon } from "lucide-react";

const History = () => {
  const navigate = useNavigate();

  const handleSelectScan = (scanId) => {
    navigate(`/results/${scanId}`);
  };

  return (
    <div>
      <div className="flex items-center gap-3 mb-8">
        <HistoryIcon className="w-8 h-8 text-blue-600" />
        <h1 className="text-3xl font-bold text-gray-900">Scan History</h1>
      </div>
      <div className="bg-white rounded-lg shadow-lg p-6">
        <HistoryTable onSelectScan={handleSelectScan} />
      </div>
    </div>
  );
};

export default History;

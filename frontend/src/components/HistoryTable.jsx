import { useEffect, useState } from "react";
import { scanAPI } from "../services/api";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Loader2, ChevronLeft, ChevronRight } from "lucide-react";

const HistoryTable = ({ onSelectScan }) => {
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    fetchHistory();
  }, [page]);

  const fetchHistory = async () => {
    try {
      const response = await scanAPI.getHistory(page, 10);
      setHistory(response.data.content);
      setTotalPages(response.data.totalPages);
    } catch (error) {
      console.error("Failed to fetch history:", error);
    } finally {
      setLoading(false);
    }
  };

  const getRiskBadgeVariant = (riskLevel) => {
    const variants = {
      LOW: "success",
      MEDIUM: "warning",
      HIGH: "destructive",
      CRITICAL: "destructive",
    };
    return variants[riskLevel] || "default";
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center py-12">
        <Loader2 className="w-8 h-8 animate-spin text-blue-600" />
        <span className="ml-2 text-gray-600">Loading history...</span>
      </div>
    );
  }

  if (history.length === 0) {
    return (
      <div className="text-center py-12">
        <p className="text-gray-500">
          No scan history found. Run your first scan!
        </p>
      </div>
    );
  }

  return (
    <div className="space-y-4">
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Identifier</TableHead>
            <TableHead>Score</TableHead>
            <TableHead>Risk Level</TableHead>
            <TableHead>Breaches</TableHead>
            <TableHead>Date</TableHead>
            <TableHead>Action</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {history.map((scan) => (
            <TableRow key={scan.scanId}>
              <TableCell className="font-medium">{scan.identifier}</TableCell>
              <TableCell>
                <span className="font-semibold">{scan.finalScore}</span>/100
              </TableCell>
              <TableCell>
                <Badge variant={getRiskBadgeVariant(scan.riskLevel)}>
                  {scan.riskLevel}
                </Badge>
              </TableCell>
              <TableCell>{scan.breachCount}</TableCell>
              <TableCell>
                {new Date(scan.timestamp).toLocaleDateString()}
              </TableCell>
              <TableCell>
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => onSelectScan(scan.scanId)}
                >
                  View Details
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>

      <div className="flex justify-between items-center pt-4">
        <Button
          variant="outline"
          size="sm"
          onClick={() => setPage((p) => Math.max(0, p - 1))}
          disabled={page === 0}
        >
          <ChevronLeft className="w-4 h-4 mr-1" />
          Previous
        </Button>
        <span className="text-sm text-gray-600">
          Page {page + 1} of {totalPages || 1}
        </span>
        <Button
          variant="outline"
          size="sm"
          onClick={() => setPage((p) => p + 1)}
          disabled={page >= totalPages - 1}
        >
          Next
          <ChevronRight className="w-4 h-4 ml-1" />
        </Button>
      </div>
    </div>
  );
};

export default HistoryTable;

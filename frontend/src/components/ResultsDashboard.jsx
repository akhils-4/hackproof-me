import { useEffect, useState } from "react";
import { scanAPI } from "../services/api";
import ScoreGauge from "./ScoreGauge";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Alert, AlertDescription } from "@/components/ui/alert";
import {
  Shield,
  AlertTriangle,
  CheckCircle,
  XCircle,
  Loader2,
} from "lucide-react";

const ResultsDashboard = ({ scanId }) => {
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchResults = async () => {
      try {
        const response = await scanAPI.getScanById(scanId);
        setResult(response.data);
      } catch (error) {
        console.error("Failed to fetch results:", error);
      } finally {
        setLoading(false);
      }
    };

    if (scanId) {
      fetchResults();
    }
  }, [scanId]);

  if (loading) {
    return (
      <div className="flex items-center justify-center py-12">
        <Loader2 className="w-8 h-8 animate-spin text-blue-600" />
        <span className="ml-2 text-gray-600">Loading results...</span>
      </div>
    );
  }

  if (!result) {
    return (
      <div className="text-center py-8 text-gray-600">No results found</div>
    );
  }

  const getRiskColor = (riskLevel) => {
    const colors = {
      LOW: "text-green-600 bg-green-50 border-green-200",
      MEDIUM: "text-yellow-600 bg-yellow-50 border-yellow-200",
      HIGH: "text-orange-600 bg-orange-50 border-orange-200",
      CRITICAL: "text-red-600 bg-red-50 border-red-200",
    };
    return colors[riskLevel] || "text-gray-600 bg-gray-50 border-gray-200";
  };

  const getRiskIcon = (riskLevel) => {
    switch (riskLevel) {
      case "LOW":
        return <CheckCircle className="w-6 h-6" />;
      case "MEDIUM":
        return <Shield className="w-6 h-6" />;
      case "HIGH":
        return <AlertTriangle className="w-6 h-6" />;
      case "CRITICAL":
        return <XCircle className="w-6 h-6" />;
      default:
        return null;
    }
  };

  return (
    <div className="space-y-6 max-w-4xl mx-auto">
      {/* Overall Score */}
      <div className="text-center">
        <ScoreGauge score={result.finalScore} riskLevel={result.riskLevel} />
        <div
          className={`inline-flex items-center gap-2 mt-6 px-6 py-3 rounded-full border-2 ${getRiskColor(
            result.riskLevel
          )}`}
        >
          {getRiskIcon(result.riskLevel)}
          <span className="font-semibold text-lg">{result.riskLevel} Risk</span>
        </div>
      </div>

      {/* Metric Cards */}
      <div className="grid md:grid-cols-3 gap-4">
        <Card>
          <CardHeader>
            <CardTitle className="text-sm font-medium text-gray-600">
              Data Breaches
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold text-gray-900">
              {result.breachCount}
            </div>
            <p className="text-xs text-gray-500 mt-1">
              {result.breachCount === 0
                ? "No breaches found"
                : "Breaches detected"}
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="text-sm font-medium text-gray-600">
              Password Strength
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold text-gray-900">
              {result.passwordScore}/100
            </div>
            <p className="text-xs text-gray-500 mt-1">
              {result.passwordScore >= 80
                ? "Strong"
                : result.passwordScore >= 60
                ? "Moderate"
                : "Weak"}
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="text-sm font-medium text-gray-600">
              Exposure Score
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold text-gray-900">
              {result.exposureScore}/100
            </div>
            <p className="text-xs text-gray-500 mt-1">
              {result.exposureScore >= 70 ? "Low exposure" : "High exposure"}
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Suggestions */}
      {result.suggestions && result.suggestions.length > 0 && (
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <AlertTriangle className="w-5 h-5 text-orange-500" />
              Security Recommendations
            </CardTitle>
          </CardHeader>
          <CardContent>
            <ul className="space-y-3">
              {result.suggestions.map((suggestion, index) => (
                <li
                  key={index}
                  className="flex items-start gap-3 p-3 bg-gray-50 rounded-lg"
                >
                  <div className="w-6 h-6 rounded-full bg-orange-100 flex items-center justify-center flex-shrink-0 mt-0.5">
                    <span className="text-xs font-semibold text-orange-600">
                      {index + 1}
                    </span>
                  </div>
                  <span className="text-sm text-gray-700">{suggestion}</span>
                </li>
              ))}
            </ul>
          </CardContent>
        </Card>
      )}

      {/* Scan Info */}
      <Alert>
        <AlertDescription className="text-xs text-gray-600">
          <strong>Scan completed:</strong>{" "}
          {new Date(result.timestamp).toLocaleString()} •
          <strong> Scan ID:</strong> {result.scanId}
        </AlertDescription>
      </Alert>
    </div>
  );
};

export default ResultsDashboard;

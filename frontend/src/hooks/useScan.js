import { useState } from 'react';
import { scanAPI } from '../services/api';

export const useScan = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [result, setResult] = useState(null);

  const runScan = async (formData) => {
    setLoading(true);
    setError(null);
    
    try {
      const response = await scanAPI.createScan(formData);
      setResult(response.data);
      return response.data;
    } catch (err) {
      setError(err.message || 'Failed to run scan');
      throw err;
    } finally {
      setLoading(false);
    }
  };

  const getScan = async (scanId) => {
    setLoading(true);
    setError(null);
    
    try {
      const response = await scanAPI.getScanById(scanId);
      setResult(response.data);
      return response.data;
    } catch (err) {
      setError(err.message || 'Failed to fetch scan');
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return {
    loading,
    error,
    result,
    runScan,
    getScan
  };
};

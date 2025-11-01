import { PieChart, Pie, Cell, ResponsiveContainer } from "recharts";

const ScoreGauge = ({ score, riskLevel }) => {
  const getScoreColor = (score) => {
    if (score >= 80) return "#22c55e";
    if (score >= 60) return "#eab308";
    if (score >= 40) return "#f97316";
    return "#ef4444";
  };

  const data = [{ value: score }, { value: 100 - score }];

  return (
    <div className="flex flex-col items-center">
      <ResponsiveContainer width={300} height={200}>
        <PieChart>
          <Pie
            data={data}
            cx="50%"
            cy="70%"
            startAngle={180}
            endAngle={0}
            innerRadius={80}
            outerRadius={110}
            paddingAngle={0}
            dataKey="value"
          >
            <Cell fill={getScoreColor(score)} />
            <Cell fill="#e5e7eb" />
          </Pie>
        </PieChart>
      </ResponsiveContainer>
      <div className="text-center -mt-20">
        <div
          className="text-5xl font-bold"
          style={{ color: getScoreColor(score) }}
        >
          {score}
        </div>
        <p className="text-sm text-gray-600 mt-2">Overall Security Score</p>
      </div>
    </div>
  );
};

export default ScoreGauge;

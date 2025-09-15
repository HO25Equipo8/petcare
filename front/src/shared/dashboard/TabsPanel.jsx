import { Tabs, TabsContent, TabsList, TabsTrigger } from '../ui/Tabs';

/**
 * TabsPanel - Reusable tab panel component
 * @param {Object} props
 * @param {string} props.defaultValue - The default selected tab value
 * @param {Array} props.tabs - Array of tab objects: { value, label, content }
 * @param {string} [props.className] - Optional className for the Tabs root
 */
export default function TabsPanel({ defaultValue, tabs, className = '' }) {
  return (
    <Tabs defaultValue={defaultValue} className={className}>
      <TabsList>
        {tabs.map(tab => (
          <TabsTrigger key={tab.value} value={tab.value}>
            {tab.label}
          </TabsTrigger>
        ))}
      </TabsList>
      {tabs.map(tab => (
        <TabsContent key={tab.value} value={tab.value} className="space-y-6">
          {tab.content}
        </TabsContent>
      ))}
    </Tabs>
  );
}

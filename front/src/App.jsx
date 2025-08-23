import { useState } from "react";
import { Theme, Flex, Box, Heading, Text, Link, Button, Card } from "@radix-ui/themes";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";

const App = () => {
	const [count, setCount] = useState(0);

	{/* Implementación de pantalla por default de Vite+React con Radix UI */}
	return (
		<Theme appearance="dark">
			<Flex 
				direction="column" 
				align="center" 
				justify="center" 
				p="4" 
				height="100vh" 
				gap="5"
			>
				{/* Logos */}
				<Flex gap="6" mb="10" align="center">
					<Link href="https://vite.dev" target="_blank">
						<img src={viteLogo} alt="Vite logo" width="96" height="96" />
					</Link>
					<Link href="https://react.dev" target="_blank">
						<img src={reactLogo} alt="React logo" width="96" height="96" />
					</Link>
				</Flex>

				{/* Título */}
				<Heading size="4" mb="8">Vite + React</Heading>

				{/* Tarjeta con contador */}
				<Card size="2" variant="surface" style={{ padding: "32px" }}>
					<Flex direction="column" align="center" gap="6">
						<Button onClick={() => setCount((c) => c + 1)}>
							count is {count}
						</Button>
						<Text color="gray" size="2" as="p">
							Edit <Text as="code">src/App.jsx</Text> and save to test HMR
						</Text>
					</Flex>
				</Card>

				{/* Texto inferior */}
				<Text color="gray" size="1" mt="10">
					Click on the Vite and React logos to learn more
				</Text>
			</Flex>
		</Theme>
	);
}

export default App;

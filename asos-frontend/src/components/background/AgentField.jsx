import { Canvas } from '@react-three/fiber';
import { Float, Icosahedron, Sphere, Sparkles } from '@react-three/drei';
import { useRef } from 'react';
import { useFrame } from '@react-three/fiber';

// Each orbiting node maps to one ASOS agent module, matching the color
// used for that module throughout the 2D UI (see index.css tokens).
const AGENT_NODES = [
  { color: '#0EA372', radius: 3.4, speed: 0.18, size: 0.34, offset: 0 },       // Finance
  { color: '#2F6FF0', radius: 4.1, speed: -0.14, size: 0.4, offset: 1.1 },     // Hiring
  { color: '#B0459A', radius: 3.0, speed: 0.22, size: 0.3, offset: 2.4 },      // Legal
  { color: '#FF6B35', radius: 4.6, speed: -0.1, size: 0.36, offset: 3.6 },     // Marketing
  { color: '#00AEC7', radius: 3.8, speed: 0.16, size: 0.32, offset: 4.8 },     // Analytics
  { color: '#F5A623', radius: 5.1, speed: -0.19, size: 0.26, offset: 5.6 },    // Approvals
];

function CoreAgent() {
  const ref = useRef();
  useFrame((_, delta) => {
    if (ref.current) {
      ref.current.rotation.x += delta * 0.08;
      ref.current.rotation.y += delta * 0.12;
    }
  });
  return (
    <Icosahedron ref={ref} args={[1.1, 0]}>
      <meshStandardMaterial color="#6C4CF1" roughness={0.25} metalness={0.3} emissive="#4F35C4" emissiveIntensity={0.35} />
    </Icosahedron>
  );
}

function OrbitNode({ color, radius, speed, size, offset }) {
  const ref = useRef();
  useFrame(({ clock }) => {
    const t = clock.getElapsedTime() * speed + offset;
    if (ref.current) {
      ref.current.position.x = Math.cos(t) * radius;
      ref.current.position.z = Math.sin(t) * radius;
      ref.current.position.y = Math.sin(t * 1.3) * 0.6;
    }
  });
  return (
    <Float speed={2} rotationIntensity={0.6} floatIntensity={0.6}>
      <Sphere ref={ref} args={[size, 24, 24]}>
        <meshStandardMaterial color={color} roughness={0.3} metalness={0.2} emissive={color} emissiveIntensity={0.5} />
      </Sphere>
    </Float>
  );
}

export default function AgentField({ intensity = 1 }) {
  return (
    <div className="bg-canvas-wrapper" aria-hidden="true">
      <Canvas
        camera={{ position: [0, 2.4, 9], fov: 42 }}
        dpr={[1, 1.5]}
        gl={{ antialias: true, alpha: true }}
      >
        <ambientLight intensity={0.5} />
        <pointLight position={[6, 6, 6]} intensity={40} color="#EFE9FF" />
        <pointLight position={[-6, -3, -4]} intensity={20} color="#FF6B35" />

        <group scale={intensity}>
          <CoreAgent />
          {AGENT_NODES.map((node, i) => (
            <OrbitNode key={i} {...node} />
          ))}
        </group>

        <Sparkles count={40} scale={12} size={2} speed={0.3} opacity={0.35} color="#EFE9FF" />
      </Canvas>
    </div>
  );
}

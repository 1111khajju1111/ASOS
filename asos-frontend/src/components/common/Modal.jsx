import { motion, AnimatePresence } from 'framer-motion';

export default function Modal({ title, onClose, children }) {
  return (
    <AnimatePresence>
      <motion.div
        className="modal-backdrop"
        onClick={onClose}
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        exit={{ opacity: 0 }}
        transition={{ duration: 0.18 }}
      >
        <motion.div
          className="modal"
          onClick={(e) => e.stopPropagation()}
          initial={{ opacity: 0, scale: 0.94, y: 12 }}
          animate={{ opacity: 1, scale: 1, y: 0 }}
          exit={{ opacity: 0, scale: 0.96, y: 8 }}
          transition={{ duration: 0.22, ease: [0.22, 1, 0.36, 1] }}
        >
          <div className="modal-header">
            <h3>{title}</h3>
            <button className="modal-close" onClick={onClose} aria-label="Close">
              ×
            </button>
          </div>
          {children}
        </motion.div>
      </motion.div>
    </AnimatePresence>
  );
}

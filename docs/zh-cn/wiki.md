# Wiki

[返回首页](/)

## KVM

QEMU中的KVM（Kernel-based Virtual Machine）是一种用于虚拟化的模块，利用Linux内核技术提高虚拟机性能。在Android上，由于硬件和内核支持的限制，KVM在部分设备上无法正常工作，影响了虚拟化性能。

## FAQ

- **Q: 为什么使用MTTCG能提高QEMU性能？**

- A: MTTCG（Multithreaded Tiny Code Generator）是QEMU的多线程代码生成器，它允许同时生成多个虚拟CPU的指令。这种并行处理提高了模拟器的整体性能，特别是在多核处理器上，加速了指令翻译过程，提高了仿真速度。

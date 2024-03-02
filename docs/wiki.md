# Wiki

[Back to homepage](/)

## KVM

KVM (Kernel-based Virtual Machine) in QEMU is a virtualization module that leverages Linux kernel technologies to enhance VM performance. On Android, due to hardware and kernel support limitations, KVM may not function properly on some devices, impacting virtualization performance.

## FAQ

- **Q: Why does using MTTCG improve QEMU performance?**

- A: MTTCG (Multithreaded Tiny Code Generator) is QEMU's multithreaded code generator, enabling the simultaneous generation of instructions for multiple virtual CPUs. This parallel processing enhances the overall performance of the emulator, especially on multi-core processors. It accelerates the instruction translation process, thereby improving simulation speed.

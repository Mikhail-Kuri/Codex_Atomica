import os
import sys

def check_quit(value):
    if value.strip().lower() == "q":
        print("\n👋 Quitter le script...")
        sys.exit(0)

# ---------------------------
# 1. TYPE MENU
# ---------------------------
print("\nChoose commit type:")
print("[f] feature")
print("[x] fix")
print("[r] refactor")
print("[d] docs")
print("[c] chore")
print("[q] quit")

choice = input("\nType (letter): ").strip().lower()
check_quit(choice)

types = {
    "f": "feature",
    "x": "fix",
    "r": "refactor",
    "d": "docs",
    "c": "chore"
}

commit_type = types.get(choice, "chore")

# ---------------------------
# 2. DESCRIPTION
# ---------------------------
description = input("Description: ").strip()
check_quit(description)

# ---------------------------
# 3. PUSH ?
# ---------------------------
push_choice = input("Push? (y/n): ").strip().lower()
check_quit(push_choice)

# ---------------------------
# 4. MESSAGE
# ---------------------------
commit_message = f"{commit_type}: {description}"

# ---------------------------
# 5. GIT ADD + COMMIT
# ---------------------------
print("\n📦 staging...")
os.system("git add -A")

print("💬 committing...")
os.system(f'git commit -m "{commit_message}"')

# ---------------------------
# 6. PUSH OPTIONAL
# ---------------------------
if push_choice == "y":
    print("🚀 pushing...")
    os.system("git push")
    print("✅ pushed!")
else:
    print("⏸️ no push")

# ---------------------------
# 7. DONE
# ---------------------------
print("\n🔥 DONE")
print(commit_message)
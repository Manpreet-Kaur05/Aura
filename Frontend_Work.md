# Frontend Development - Aura

**Developer:** Manpreet  
**Role:** Frontend UI/UX Implementation  
**Duration:** October 2025  
**Team:** Oorja, Khushi, Gauri, Manpreet

---

## Overview

I was responsible for designing and implementing the complete user interface for Aura, our mental health companion app built for Siemens CodeConnect 2025 hackathon.

## Screens Developed

### 1. Mood Selection Interface
- Custom emotion selector with 6 mood options
- Sliding intensity scale (1-10)
- Optional journal entry field
- Visual feedback on selection
- Material Design emotion buttons

### 2. Dashboard
- Weekly mood visualization chart
- Streak counter for consistent logging
- Quick action cards
- Motivational daily quotes
- Recent activity timeline
- Statistics summary cards

### 3. Multimodal Check-in Screen
- Camera preview interface
- 30-second recording controls
- Real-time face detection overlay
- Progress indicator
- Recording status display

### 4. Anonymous Vent Board
- Post creation interface
- Feed with card-based layout
- Emoji reaction buttons
- Report functionality
- Anonymous identity protection

### 5. AI Chatbot Interface
- Chat message layout
- Text input field
- Send button with loading state
- Typing indicators
- Message bubbles design

### 6. Crisis Resources
- Emergency helpline cards
- Breathing exercise interface
- Quick access buttons
- Resource categorization

## Design Philosophy

**Goal:** Create a calming, accessible interface that doesn't overwhelm users dealing with mental health challenges.

**Key Principles:**
- Soothing color palette (blues and purples)
- Ample whitespace to reduce cognitive load
- Large, easy-to-tap buttons (48dp minimum)
- Clear visual hierarchy
- Smooth animations and transitions

## Technical Implementation

### Technologies Used
- **Language:** Java (Android)
- **UI Framework:** Material Design Components
- **Layouts:** ConstraintLayout, LinearLayout, CardView
- **Navigation:** Bottom Navigation + Fragment-based navigation
- **Lists:** RecyclerView with custom adapters

### Architecture
- MVVM pattern for UI layer
- Activities and Fragments for screens
- ViewModels for UI logic
- Data binding for reactive updates

### Responsive Design
- Multiple screen size support
- Landscape and portrait orientations
- Tablet-optimized layouts
- Accessibility features (content descriptions, large text support)

## UI Components Created

### Custom Mood Selector
- 6 emotion buttons with emojis
- Color-coded for quick recognition
- Tap feedback animations
- Material ripple effects

### Intensity Slider
- Custom slider with mood-specific colors
- Value display (1-10 scale)
- Smooth interaction
- Visual feedback

### Mood History Calendar
- Calendar view of logged moods
- Color-coded days
- Tap to view entry details
- Month navigation

### Dashboard Cards
- Material Design elevated cards
- Consistent padding and margins
- Smooth shadow effects
- Information hierarchy

## Design Decisions

### Color Psychology
- **Blue (#2196F3):** Trust, calmness
- **Purple (#9C27B0):** Wisdom, spirituality
- **Soft tones:** Avoid overstimulation

### Typography
- **Roboto** font family (Android standard)
- Large sizes for readability
- Proper heading hierarchy
- Consistent text styles

### Spacing & Layout
- 8dp baseline grid
- Consistent margins (16dp standard)
- Breathing room between elements
- Card-based organization

### Accessibility
- Minimum 48dp touch targets
- 4.5:1 color contrast ratios
- Content descriptions for screen readers
- Support for system font sizes

## Challenges Solved

1. **Complex mood chart visualization**
   - Solution: Used MPAndroidChart library
   - Customized for mental health context
   - Smooth animations

2. **Smooth camera integration**
   - Solution: CameraX library
   - Proper lifecycle management
   - Permission handling

3. **Responsive mood calendar**
   - Solution: Custom calendar adapter
   - Efficient date handling
   - Performance optimization

4. **Performance optimization**
   - Solution: ViewHolder pattern in RecyclerView
   - Image loading optimization
   - Lazy loading for lists

## Files Created/Modified
app/src/main/res/
├── layout/
│ ├── activity_mood_selection.xml
│ ├── fragment_dashboard.xml
│ ├── activity_multimodal.xml
│ ├── activity_vent.xml
│ ├── activity_chatbot.xml
│ ├── activity_crisis_resources.xml
│ └── [12+ more layout files]
├── values/
│ ├── colors.xml (mood-specific colors)
│ ├── strings.xml (all UI text)
│ └── styles.xml (custom button/card styles)
└── drawable/
└── [various UI assets]

text

## Team Collaboration

While I owned the frontend layer, I collaborated closely with:
- **Oorja:** Backend integration, overall architecture
- **Khushi:** ViewModels and data binding
- **Gauri:** Database design and ML features

## Post-Hackathon Improvements

After the hackathon, I've enhanced the UI with:
- Improved color accessibility
- Standardized dimensions
- Better documentation
- Component style library
- Resource organization

---

**Original Team Repository:** [oorjatiwari23/Aura](https://github.com/oorjatiwari23/Aura)  
**My Enhanced Fork:** [Manpreet-Kaur05/Aura](https://github.com/Manpreet-Kaur05/Aura)

*Last Updated: October 18, 2025*
